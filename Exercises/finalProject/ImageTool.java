package Exercises.finalProject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private ListView<Image> thumbnailList = new ListView<>();
    private TextArea metadataArea = new TextArea();

    public ImageToolView(Stage stage) {
        this.stage = stage;
        Button uploadBtn = new Button("Upload Image");
        Button convertBtn = new Button("Convert to...");
        Button downloadBtn = new Button("Download");

        uploadBtn.setOnAction(e -> controller.handleUpload());
        convertBtn.setOnAction(e -> controller.handleConvert());
        downloadBtn.setOnAction(e -> controller.handleDownload());

        root.getChildren().addAll(uploadBtn, thumbnailList, metadataArea, convertBtn, downloadBtn);
        stage.setScene(new Scene(root, 600, 400));
    }

    public void displayThumbnails(List<Image> images) {
        thumbnailList.getItems().addAll(images);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(view.getStage());

        if (files != null) {
            List<Image> thumbnails = new ArrayList<>();
            for (File file : files) {
                Image img = model.loadImage(file);
                thumbnails.add(img);
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
