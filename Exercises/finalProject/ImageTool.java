package Exercises.finalProject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

// ===== 1. Main Class (Entry Point) =====
public class ImageTool extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageModel model = new ImageModel();
        ImageToolView view = new ImageToolView(primaryStage);
        ImageToolController controller = new ImageToolController(model, view);
        
        view.setController(controller);
        primaryStage.setTitle("Image Management Tool");
        primaryStage.show();
    }
}

// ===== 2. Model (Handles Image Processing) =====
class ImageModel {
    private List<File> imageFiles = new ArrayList<>();

    public Image loadImage(File file) {
        try {
            return new Image(file.toURI().toString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + e.getMessage());
        }
    }

    public Image loadAndResizeImage(File file, int width, int height) {
        try {
            // Load original image
            Image originalImage = new Image(file.toURI().toString());
            
            // Create resized version
            ImageView imageView = new ImageView(originalImage);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            
            // Render to new image
            return imageView.snapshot(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to resize image: " + e.getMessage());
        }
    }

    public String getImageMetadata(File file) {
        StringBuilder metadata = new StringBuilder();
        
        // Basic metadata
        try {
            BufferedImage img = ImageIO.read(file);
            metadata.append(String.format(
                "Basic Info:\nWidth: %d px\nHeight: %d px\nFormat: %s\n\n",
                img.getWidth(), img.getHeight(), getFileExtension(file)
            ));
        } catch (Exception e) {
            metadata.append("Couldn't read basic image properties\n");
        }
        
        // EXIF metadata (if available)
        try {
            Metadata exifData = ImageMetadataReader.readMetadata(file);
            metadata.append("EXIF Data:\n");
            
            // Get common EXIF tags
            ExifSubIFDDirectory exifDir = exifData.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (exifDir != null) {
                metadata.append(String.format(
                    "Camera: %s %s\n",
                    exifDir.getString(ExifSubIFDDirectory.TAG_MAKE),
                    exifDir.getString(ExifSubIFDDirectory.TAG_MODEL)
                ));
                metadata.append(String.format(
                    "Date Taken: %s\n",
                    exifDir.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
                ));
            }
            
            // GPS data
            GpsDirectory gpsDir = exifData.getFirstDirectoryOfType(GpsDirectory.class);
            if (gpsDir != null) {
                metadata.append(String.format(
                    "Location: %s, %s\n",
                    gpsDir.getGeoLocation().getLatitude(),
                    gpsDir.getGeoLocation().getLongitude()
                ));
            }
            
        } catch (Exception e) {
            metadata.append("No EXIF data available\n");
        }
        
        return metadata.toString();
    }

    public File convertImage(File input, String format) throws IOException {
        BufferedImage image = ImageIO.read(input);
        if (image == null) {
            throw new IOException("Unsupported image format");
        }

        // Create output filename
        String outputName = input.getName().replaceFirst("[.][^.]+$", "") + 
                          "_converted." + format.toLowerCase();
        File output = new File(input.getParent(), outputName);

        // Convert based on format
        boolean success;
        switch (format.toLowerCase()) {
            case "jpeg":
            case "jpg":
                success = ImageIO.write(image, "jpg", output);
                break;
            case "png":
                success = ImageIO.write(image, "png", output);
                break;
            case "bmp":
                success = ImageIO.write(image, "bmp", output);
                break;
            case "gif":
                success = ImageIO.write(image, "gif", output);
                break;
            default:
                throw new IOException("Unsupported output format: " + format);
        }

        if (!success) {
            throw new IOException("Failed to convert image to " + format);
        }

        return output;
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }
}

// ===== 3. View (GUI) =====
class ImageToolView {
    private Stage stage;
    private ImageToolController controller;
    private VBox root = new VBox(10);
    private FlowPane thumbnailContainer = new FlowPane(); // Changed from ListView
    private TextArea metadataArea = new TextArea();
    private ComboBox<String> formatComboBox = new ComboBox<>();
    private Button convertBtn = new Button("Convert");
    private Button downloadBtn = new Button("Download");

    public ImageToolView(Stage stage) {
        this.stage = stage;
        Button uploadBtn = new Button("Upload Image");

        // Configure UI components
        thumbnailContainer.setHgap(10);
        thumbnailContainer.setVgap(10);
        thumbnailContainer.setPrefWrapLength(400);
        
        metadataArea.setEditable(false);
        metadataArea.setWrapText(true);
        metadataArea.setPrefHeight(150);
        
        // Setup format selection
        formatComboBox.getItems().addAll("JPEG", "PNG", "BMP", "GIF");
        formatComboBox.setValue("JPEG");
        
        // Disable download initially
        downloadBtn.setDisable(true);

        ScrollPane scrollPane = new ScrollPane(thumbnailContainer);
        scrollPane.setFitToWidth(true);

        // Layout
        HBox conversionBox = new HBox(10, 
            new Label("Convert to:"), 
            formatComboBox, 
            convertBtn,
            downloadBtn
        );

        root.getChildren().addAll(
            uploadBtn,
            scrollPane,
            metadataArea,
            conversionBox
        );

        // Set button actions
        uploadBtn.setOnAction(e -> controller.handleUpload());
        convertBtn.setOnAction(e -> controller.handleConvert());
        downloadBtn.setOnAction(e -> controller.handleDownload());

        stage.setScene(new Scene(root, 650, 500));
    }

    public String getSelectedFormat() {
        return formatComboBox.getValue();
    }

    public void enableDownload(boolean enable) {
        downloadBtn.setDisable(!enable);
    }

    public void displayThumbnails(List<Image> thumbnails) {
        thumbnailContainer.getChildren().clear();
        for (Image thumbnail : thumbnails) {
            ImageView imageView = new ImageView(thumbnail);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            thumbnailContainer.getChildren().add(imageView);
        }
    }

    public void clearThumbnails() {
        thumbnailContainer.getChildren().clear();
    }
    
    public void addThumbnail(ImageView imageView) {
        thumbnailContainer.getChildren().add(imageView);
    }
    
    public FlowPane getThumbnailContainer() {
        return thumbnailContainer;
    }

    public void showMetadata(String metadata) {
        metadataArea.setText(metadata);
    }

    public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

    public void showSuccess(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    public void setController(ImageToolController controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }
}

// ===== 4. Controller (Handles User Actions) =====
class ImageToolController {
    private ImageModel model;
    private ImageToolView view;
    private Map<ImageView, File> imageFileMap = new HashMap<>();
    private List<File> convertedFiles = new ArrayList<>();

    public ImageToolController(ImageModel model, ImageToolView view) {
        this.model = model;
        this.view = view;
    }

    public void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.bmp", "*.gif"));
        List<File> files = fileChooser.showOpenMultipleDialog(view.getStage());

        if (files != null) {
            view.clearThumbnails();
            imageFileMap.clear();
            
            for (File file : files) {
                Image thumbnail = model.loadAndResizeImage(file, 100, 100);
                ImageView imageView = new ImageView(thumbnail);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                
                // Store reference to original file
                imageFileMap.put(imageView, file);
                
                // Show metadata when clicked
                imageView.setOnMouseClicked(e -> showMetadataForImage(imageView));
                
                view.addThumbnail(imageView);
            }
        }
    }

    private void showMetadataForImage(ImageView imageView) {
        File file = imageFileMap.get(imageView);
        if (file != null) {
            view.showMetadata(model.getImageMetadata(file));
        }
    }

    public void handleConvert() {
        if (imageFileMap.isEmpty()) {
            view.showError("Please upload images first");
            return;
        }

        convertedFiles.clear();
        String format = view.getSelectedFormat();

        new Thread(() -> {
            try {
                for (File originalFile : imageFileMap.values()) {
                    File convertedFile = model.convertImage(originalFile, format);
                    convertedFiles.add(convertedFile);
                }
                
                javafx.application.Platform.runLater(() -> {
                    view.enableDownload(true);
                    view.showSuccess(convertedFiles.size() + " images converted successfully!");
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    view.showError("Conversion failed: " + e.getMessage());
                });
            }
        }).start();
    }

    public void handleDownload() {
        if (convertedFiles.isEmpty()) {
            view.showError("No converted files available");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Converted Images");
        fileChooser.setInitialFileName("converted_images");
        
        // Set extension filter
        String format = view.getSelectedFormat().toLowerCase();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(format.toUpperCase() + " Files", "*." + format)
        );

        File destination = fileChooser.showSaveDialog(view.getStage());
        if (destination != null) {
            try {
                String dirPath = destination.getParent();
                for (File convertedFile : convertedFiles) {
                    String newPath = dirPath + File.separator + convertedFile.getName();
                    Files.copy(convertedFile.toPath(), new File(newPath).toPath(), 
                             StandardCopyOption.REPLACE_EXISTING);
                }
                view.showSuccess("Files saved successfully!");
            } catch (Exception e) {
                view.showError("Failed to save files: " + e.getMessage());
            }
        }
    }
}

// ===== 5. Factory Pattern (Image Conversion) =====
interface ImageConverter {
    File convert(File input);
}

class JpegConverter implements ImageConverter {
    @Override
    public File convert(File input) {
        try {
            BufferedImage img = ImageIO.read(input);
            File output = new File(input.getParent(), "converted.jpg");
            ImageIO.write(img, "jpg", output);
            return output;
        } catch (Exception e) {
            throw new RuntimeException("JPEG conversion failed: " + e.getMessage());
        }
    }
}

class PngConverter implements ImageConverter {
    @Override
    public File convert(File input) {
        try {
            BufferedImage img = ImageIO.read(input);
            File output = new File(input.getParent(), "converted.png");
            ImageIO.write(img, "png", output);
            return output;
        } catch (Exception e) {
            throw new RuntimeException("PNG conversion failed: " + e.getMessage());
        }
    }
}

class ImageConverterFactory {
    public static ImageConverter getConverter(String format) {
        switch (format.toLowerCase()) {
            case "jpeg": return new JpegConverter();
            case "png": return new PngConverter();
            case "bmp": return new BmpConverter();
            default: throw new IllegalArgumentException("Unsupported format");
        }
    }
}

class BmpConverter implements ImageConverter {
    @Override
    public File convert(File input) {
        try {
            BufferedImage img = ImageIO.read(input);
            File output = new File(input.getParent(), "converted.bmp");
            ImageIO.write(img, "bmp", output);
            return output;
        } catch (Exception e) {
            throw new RuntimeException("BMP conversion failed: " + e.getMessage());
        }
    }
}
