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
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author flashomer
 */
public class scene2_plane_sphere extends Application {
    
        
    private static int sizeScale = 50;
    private static int WIDTH = 100;
    private static int HEIGHT = 100; 
    private static int size;
    private static int angle;
    private static JSONArray  up;
    private static JSONArray  direction;
    private static JSONArray  dColor;
    private static JSONArray  bgColor;
    private static JSONArray  bgAmbient;
    private static JSONArray  groups;
    private static JSONArray  light;
    private static JSONArray  materials;
    private static String jFileName;
      
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        jFileName             = "scene2_plane_sphere";
        
        JSONObject parsinJson = readJson.getreadJson("input/"+jFileName+".json");
        
        JSONObject perspectivecamera    = parsinJson.getJSONObject("perspectivecamera");
        JSONObject background           = parsinJson.getJSONObject("background");
        groups                   = parsinJson.getJSONArray("group");
        light                   = parsinJson.getJSONArray("lights");
        materials               = parsinJson.getJSONArray("materials");
        direction               = light.getJSONObject(0).getJSONObject("directionalLight").getJSONArray("direction");
        dColor                  = light.getJSONObject(0).getJSONObject("directionalLight").getJSONArray("color");
        size                    = 5;
        angle                   = Integer.parseInt(perspectivecamera.optString("angle").toString());
        bgColor                 = background.getJSONArray("color");
        bgAmbient               = background.getJSONArray("ambient");
        up                      = perspectivecamera.getJSONArray("up");
     
        launch(args);
        
	}
    
    public void start(Stage primaryStage) throws Exception {

        Group group = new Group();

                
        //group.getChildren().add(box);
        
        
        
        for(int i=groups.length()-1; i>-1; i--){
            
        //System.out.println("i: "+i+" Grup: "+groups.getJSONObject(i).names().getString(0) );
        
        if(groups.getJSONObject(i).names().getString(0).equals("plane")) {
            
            group.getChildren().add( addPlane(groups.getJSONObject(i).getJSONObject("plane").getJSONArray("normal"),
                    Double.parseDouble(groups.getJSONObject(i).getJSONObject("plane").optString("offset").toString() ),
                    materials.getJSONObject(i).getJSONObject("phongMaterial").getJSONArray("diffuseColor") )); 
            
        } else {
            
          group.getChildren().add( addSphere( groups.getJSONObject(i).getJSONObject("sphere").getJSONArray("center"),
                    Double.parseDouble(groups.getJSONObject(i).getJSONObject("sphere").optString("radius").toString() ),
                    materials.getJSONObject(i).getJSONObject("phongMaterial").getJSONArray("diffuseColor") ) );  
        }
            
        }
        
        
        group.getChildren().addAll(addLight());
        group.getChildren().add(addAmbient());
 
        //Create new Camera
        Camera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(group, WIDTH*size, HEIGHT*size);
        scene.setFill(Color.color( bgColor.getDouble(0),bgColor.getDouble(1),bgColor.getDouble(2) ));
        //Attach to scene
        scene.setCamera(camera);

        
        camera.rotateProperty().set(angle);
        
        camera.translateXProperty().set(up.getDouble(0));
        camera.translateYProperty().set(up.getDouble(1));
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

    private Sphere addSphere(JSONArray center, double radius, JSONArray diffuseColor){


          Sphere sphere = new Sphere(radius*sizeScale);

          double d1 =Double.parseDouble( diffuseColor.get(0).toString() );
          double d2 =Double.parseDouble( diffuseColor.get(1).toString() );
          double d3 =Double.parseDouble( diffuseColor.get(2).toString() );
          
          

          PhongMaterial material = new PhongMaterial();
          material.setDiffuseColor(Color.color(d1, d2, d3));

          sphere.setMaterial(material);
          sphere.setTranslateX( Integer.parseInt(center.get(0).toString())*sizeScale ); 
          sphere.setTranslateY( Integer.parseInt(center.get(1).toString())*sizeScale );
          //sphere.setTranslateZ( Integer.parseInt(center.get(2).toString())*sizeScale );   


        return sphere;
    }
    
     private Box addPlane(JSONArray normal, double offset, JSONArray diffuseColor){


        Box box = new Box(600, 500, 200);
        

          double d1 =Double.parseDouble( diffuseColor.get(0).toString() );
          double d2 =Double.parseDouble( diffuseColor.get(1).toString() );
          double d3 =Double.parseDouble( diffuseColor.get(2).toString() );
        
          PhongMaterial planeMaterial = new PhongMaterial();
          planeMaterial.setDiffuseColor(Color.color(d1,d2,d3));
          
        box.rotateProperty().set(angle);

        box.setMaterial(planeMaterial);
        
        box.setTranslateX(Double.parseDouble(normal.get(0).toString())*100 );
        box.setTranslateY(Double.parseDouble(normal.get(2).toString())*100 + (offset * -280));
        box.setTranslateZ(Double.parseDouble(normal.get(1).toString())*100);


        return box;
    }   
    
    private Node addAmbient() {
        //Create light object
       
        AmbientLight light = new AmbientLight();
        
        //Set light color
        light.setColor(Color.color(bgAmbient.getDouble(0),bgAmbient.getDouble(1),bgAmbient.getDouble(2) ));
        
        return light;
    }
  
    private Node[] addLight() {
        
       //System.out.println("Light: "+direction.toString() );
        
       double d1 = Double.parseDouble( direction.get(0).toString() )*100;
       double d2 = Double.parseDouble( direction.get(2).toString() )*100;
       double d3 = Double.parseDouble( direction.get(1).toString() )*100;
       
       double dc1 = Double.parseDouble( dColor.get(0).toString() );
       double dc2 = Double.parseDouble( dColor.get(2).toString() );
       double dc3 = Double.parseDouble( dColor.get(1).toString() );


      PointLight pointLight = new PointLight(Color.color(dc1,dc2,dc3));
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
