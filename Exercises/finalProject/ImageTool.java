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
import java.util.*;


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
        try {
            BufferedImage img = ImageIO.read(file);
            return String.format(
                "Width: %d\nHeight: %d\nFormat: %s",
                img.getWidth(), img.getHeight(), getFileExtension(file)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to read metadata: " + e.getMessage());
        }
    }

    public File convertImage(File input, String format) {
        ImageConverter converter = ImageConverterFactory.getConverter(format);
        return converter.convert(input);
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

    public ImageToolView(Stage stage) {
        this.stage = stage;
        Button uploadBtn = new Button("Upload Image");
        Button convertBtn = new Button("Convert to...");
        Button downloadBtn = new Button("Download");

        // Configure thumbnail container
        thumbnailContainer.setHgap(10);
        thumbnailContainer.setVgap(10);
        thumbnailContainer.setPrefWrapLength(400); // Adjust as needed

        uploadBtn.setOnAction(e -> controller.handleUpload());
        convertBtn.setOnAction(e -> controller.handleConvert());
        downloadBtn.setOnAction(e -> controller.handleDownload());

        root.getChildren().addAll(uploadBtn, thumbnailContainer, metadataArea, convertBtn, downloadBtn);
        stage.setScene(new Scene(root, 600, 400));
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

    public void showMetadata(String metadata) {
        metadataArea.setText(metadata);
    }

    public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
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

    public ImageToolController(ImageModel model, ImageToolView view) {
        this.model = model;
        this.view = view;
    }

    public void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.bmp", "*.gif"));
        List<File> files = fileChooser.showOpenMultipleDialog(view.getStage());

        if (files != null) {
            List<Image> thumbnails = new ArrayList<>();
            for (File file : files) {
                // Load and resize to 100x100 thumbnail
                Image thumbnail = model.loadAndResizeImage(file, 100, 100);
                thumbnails.add(thumbnail);
                
                // Show metadata (optional)
                view.showMetadata(model.getImageMetadata(file));
            }
            view.displayThumbnails(thumbnails);
        }
    }

    public void handleConvert() {
        // TODO: Let user select format (JPEG, PNG, BMP) and convert
    }

    public void handleDownload() {
        // TODO: Let user download converted images
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
