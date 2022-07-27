/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg415.hw3;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author flashomer
 */
public class scene1_exponent_variations extends Application {
    
        
    private static int sizeScale = 50;
    private static int WIDTH = 100;
    private static int HEIGHT = 100; 
    private static int size; 
    private static JSONArray  direction;
    private static JSONArray  bgColor;
    private static JSONArray  groups;
    private static JSONArray  materials;
    private static String jFileName;
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        
        jFileName               = "scene1_exponent_variations";     
        
        JSONObject parsinJson = readJson.getreadJson("input/"+jFileName+".json");
        
        JSONObject orthocamera  = parsinJson.getJSONObject("orthocamera");
        JSONObject background   = parsinJson.getJSONObject("background");
        JSONObject light   = parsinJson.getJSONObject("light");
        groups                   = parsinJson.getJSONArray("group");
        materials               = parsinJson.getJSONArray("materials");
        direction               = light.getJSONArray("direction");
        size                    = 5;
        bgColor                 = background.getJSONArray("color");
     
        launch(args);
        
	}
    
    public void start(Stage primaryStage) throws Exception {

        Group group = new Group();
        
        for(int i=groups.length()-1; i>-1; i--){
            
            
        //System.out.println( "Color:"+materials.getJSONObject(i).getJSONObject("phongMaterial").getJSONArray("specularColor").toString() );  
       
        group.getChildren().add( addSphere( groups.getJSONObject(i).getJSONObject("sphere").getJSONArray("center"),
                    Double.parseDouble(groups.getJSONObject(i).getJSONObject("sphere").optString("radius").toString() ),
                    materials.getJSONObject(i).getJSONObject("phongMaterial").getJSONArray("diffuseColor"),
                materials.getJSONObject(i).getJSONObject("phongMaterial").getJSONArray("specularColor"), 
                materials.getJSONObject(i).getJSONObject("phongMaterial").getInt("exponent") ));
        }

        //Create new Camera
        Camera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(group, WIDTH*size, HEIGHT*size);
        scene.setFill(Color.color(0, 0, 0 ));
        //Attach to scene
        scene.setCamera(camera);
        
 
        //Move back a little to get a good view of the sphere
        camera.translateZProperty().set(-600);
 
        //Set the clipping planes
        camera.setNearClip(1);
        camera.setFarClip(600);
 
 
        saveAsPng(scene);
        primaryStage.setScene(scene);

        //stage.show();
        primaryStage.show();
        System.out.println("Image created");        
        
        
    }
    
    
    private Sphere addSphere(JSONArray center, double radius, JSONArray diffuseColor, JSONArray specularColor, int exponent){


          Sphere sphere = new Sphere(radius*sizeScale);
          double d1 =Double.parseDouble( diffuseColor.get(0).toString() );
          double d2 =Double.parseDouble( diffuseColor.get(1).toString() );
          double d3 =Double.parseDouble( diffuseColor.get(2).toString() );
          
          double s1 =Double.parseDouble( specularColor.get(0).toString() );
          double s2 =Double.parseDouble( specularColor.get(1).toString() );
          double s3 =Double.parseDouble( specularColor.get(2).toString() );

          PhongMaterial material = new PhongMaterial();
          material.setDiffuseColor(Color.color(d1, d2, d3));
          material.setSpecularColor(Color.color(s1, s2, s3));
          
          material.setSpecularPower(exponent);
          
          
          //exponent

          sphere.setMaterial(material);
          sphere.setTranslateX( Integer.parseInt(center.get(0).toString())*sizeScale ); 
          sphere.setTranslateY( Integer.parseInt(center.get(1).toString())*sizeScale );
          //sphere.setTranslateZ( Integer.parseInt(center.get(2).toString())*sizeScale );   


        return sphere;
    }
    

  
    public void saveAsPng(Scene scene) {
        WritableImage image = scene.snapshot(null);

            File file =  new File("output/"+jFileName+".jpg");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  
    
    
}
