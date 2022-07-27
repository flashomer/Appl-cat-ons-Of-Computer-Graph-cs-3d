/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg415.hw1;
       

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author flashomer
 */
public class Hw1 extends Application {
    
        
    private static int sizeScale = 50;
    private static int WIDTH = 100;
    private static int HEIGHT = 100; 
    private static int size; 
    private static JSONArray  direction;
    private static JSONArray  bgColor;
    private static JSONArray  groups;
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        JSONObject parsinJson = readJson.getreadJson("input/scene1.json");
        
        JSONObject orthocamera  = parsinJson.getJSONObject("orthocamera");
        JSONObject background   = parsinJson.getJSONObject("background");
        groups                   = parsinJson.getJSONArray("group");
        direction               = orthocamera.getJSONArray("direction");
        size                    = Integer.parseInt(orthocamera.optString("size").toString());
        bgColor                 = background.getJSONArray("color");
     
        launch(args);
        
	}
    
    public void start(Stage primaryStage) throws Exception {

        Group group = new Group();
        
        for(int i=groups.length()-1; i>-1; i--){
            
           // System.out.println("i: "+i+" Grup: "+groups.getJSONObject(i).getJSONObject("sphere").getJSONArray("center").toString() );
        
            group.getChildren().add( addSphere( groups.getJSONObject(i).getJSONObject("sphere").getJSONArray("center"),
                    Double.parseDouble(groups.getJSONObject(i).getJSONObject("sphere").optString("radius").toString() ),
                    groups.getJSONObject(i).getJSONObject("sphere").getJSONArray("color")) );
        }
        
        
        //group.getChildren().addAll(addLight(direction));
 
        //Create new Camera
        Camera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(group, WIDTH*size, HEIGHT*size);
        scene.setFill(Color.rgb(bgColor.getInt(0), bgColor.getInt(1), bgColor.getInt(2)));
        //Attach to scene
        scene.setCamera(camera);
 
        //Move back a little to get a good view of the sphere
        camera.translateZProperty().set(-500);
 
        //Set the clipping planes
        camera.setNearClip(1);
        camera.setFarClip(500);
 
 
        saveAsPng(scene);
        primaryStage.setScene(scene);

        //stage.show();
        primaryStage.show();
        System.out.println("Image created");        
        
        
    }

    private Sphere addSphere(JSONArray center, double radius, JSONArray color){


          Sphere sphere = new Sphere(radius*sizeScale);
          int c1 =Integer.parseInt( color.get(0).toString() );
          int c2 =Integer.parseInt( color.get(1).toString() );
          int c3 =Integer.parseInt( color.get(2).toString() );

          PhongMaterial material = new PhongMaterial();
          material.setDiffuseColor(Color.rgb(c1, c2, c3));

          sphere.setMaterial(material);
          sphere.setTranslateX( Integer.parseInt(center.get(0).toString())*sizeScale ); 
          sphere.setTranslateY( Integer.parseInt(center.get(1).toString())*sizeScale );
          //sphere.setTranslateZ( Integer.parseInt(center.get(2).toString())*sizeScale );   


        return sphere;
    }
  
    private Node[] addLight(JSONArray direction) {
        System.out.println("Light: "+direction.toString() );

      PointLight pointLight = new PointLight();
      pointLight.getTransforms().add(new Translate( Integer.parseInt(direction.get(0).toString())*100,Integer.parseInt(direction.get(1).toString())*100,Integer.parseInt(direction.get(2).toString())*100 ) );

      return new Node[]{pointLight};
    }
  
    public void saveAsPng(Scene scene) {
        WritableImage image = scene.snapshot(null);

            File file =  new File("output/scene1.jpg");
            File file2 = new File("output/scene1_depth.jpg");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                ImageIO.write(SwingFXUtils.fromFXImage(toGrayScale(image), null), "png", file2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  
    
    
    public static Image toGrayScale(Image sourceImage) {

        PixelReader pixelReader = sourceImage.getPixelReader();

        int width = (int) sourceImage.getWidth();
        int height = (int) sourceImage.getHeight();

        WritableImage grayImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelReader.getArgb(x, y);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int grayLevel = (int) (0.7 * red + 0.9 * green + 0.41 * blue);
                int gray = (alpha << 24) + (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                grayImage.getPixelWriter().setArgb(x, y, gray);
            }           
        }
        return grayImage;
    }
    

    
}
