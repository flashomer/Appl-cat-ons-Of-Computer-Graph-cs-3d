/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg415.hw2;

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
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author flashomer
 */
public class scene2_ambient extends Application {
    
        
    private static int sizeScale = 50;
    private static int WIDTH = 100;
    private static int HEIGHT = 100; 
    private static int size; 
    private static JSONArray  direction;
    private static JSONArray  bgColor;
    private static JSONArray  bgAmbient;
    private static JSONArray  groups;
    private static String jFileName;
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        jFileName             = "scene2_ambient";
        
        JSONObject parsinJson = readJson.getreadJson("input/"+jFileName+".json");
        
        JSONObject orthocamera  = parsinJson.getJSONObject("orthocamera");
        JSONObject background   = parsinJson.getJSONObject("background");
        JSONObject light   = parsinJson.getJSONObject("light");
        groups                   = parsinJson.getJSONArray("group");
        direction               = light.getJSONArray("direction");
        size                    = Integer.parseInt(orthocamera.optString("size").toString());
        bgColor                 = background.getJSONArray("color");
        bgAmbient               = background.getJSONArray("ambient");
     
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
        
        
        group.getChildren().addAll(addLight());
        group.getChildren().add(addAmbient());
 
        //Create new Camera
        Camera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(group, WIDTH*size, HEIGHT*size);
        scene.setFill(Color.color( bgColor.getDouble(0),bgColor.getDouble(1),bgColor.getDouble(2) ));
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
          material.setDiffuseColor(Color.color(c1, c2, c3));

          sphere.setMaterial(material);
          sphere.setTranslateX( Integer.parseInt(center.get(0).toString())*sizeScale ); 
          sphere.setTranslateY( Integer.parseInt(center.get(1).toString())*sizeScale );
          //sphere.setTranslateZ( Integer.parseInt(center.get(2).toString())*sizeScale );   


        return sphere;
    }
    
    
    
    private Node addAmbient() {
        //Create light object
       
        AmbientLight light = new AmbientLight();
        
        //Set light color
        light.setColor(Color.color(bgAmbient.getDouble(0),bgAmbient.getDouble(1),bgAmbient.getDouble(2) ));
        
        return light;
    }
  
    private Node[] addLight() {
        
       System.out.println("Light: "+direction.toString() );
        
       double d1 = Double.parseDouble( direction.get(0).toString() )*100;
       double d2 = Double.parseDouble( direction.get(1).toString() )*100;
       double d3 = Double.parseDouble( direction.get(2).toString() )*100;


      PointLight pointLight = new PointLight(Color.LIGHTYELLOW);
      pointLight.getTransforms().add(new Translate( d1,d2,d3 ) );

      return new Node[]{pointLight};
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
