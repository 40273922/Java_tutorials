package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Mesh{
	
	public Vector3D[] vertices;
	public Vector3D[] normals;
	public int[] indices;
	

	public Mesh(String objFilePath, String order) {
		
		//构建文件路径
		String filePath = MainThread.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		objFilePath = filePath + objFilePath;
		
		//从文件中读取顶点，法线的个数
        int numVertices = 0;
        int numNormals = 0;
        int numIndices = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(objFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    numVertices++;
                } else if (line.startsWith("vn ")) {
                    numNormals++;
                } else if (line.startsWith("f ")) {
                    numIndices += 3;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //创建用来存储顶点位置， 发件的数组
        vertices = new Vector3D[numVertices];
        normals = new Vector3D[numVertices];
        indices = new int[numIndices];
        
        //创建一个单独数组用来帮助调整法线数组索引， 这样顶点位置和法线数组的索引就能对应上来
        Vector3D[] normalsTemp = new Vector3D[numNormals];
        
        //在重新读一边这个 OBj文件
        try (BufferedReader br = new BufferedReader(new FileReader(objFilePath))) {
            String line;
            int vertexIndex = 0;
            int normalIndex = 0;
            int indexIndex = 0;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] values = line.split("\\s+");        //读取顶点位置
                    float x = Float.parseFloat(values[1]);
                    float y = Float.parseFloat(values[2]);
                    float z = Float.parseFloat(values[3]);
                    vertices[vertexIndex++] = new Vector3D(x, y, z);
                } else if (line.startsWith("vn ")) {
                    String[] values = line.split("\\s+");        //读取法线位置，存放在临时的法线数组中
                    float x = Float.parseFloat(values[1]);
                    float y = Float.parseFloat(values[2]);
                    float z = Float.parseFloat(values[3]);
                    normalsTemp[normalIndex++] = new Vector3D(x, y, z);
                } else if (line.startsWith("f ")) {
                	
                    //当文件位置到达构建三角形索引的后，我们按索引里面的信息重新构建法线数组
                	
                	String[] values = line.split("\\s+");
                    
                    if(order == "clockwise") {
	                    for (int i = 3; i >= 1; i--) {
	                        String[] indicesValues = values[i].split("/");
	                        int vertexIndexValue = Integer.parseInt(indicesValues[0]) - 1;
	                        indices[indexIndex++] = vertexIndexValue;
	                        normals[vertexIndexValue] = normalsTemp[Integer.parseInt(indicesValues[2])-1];
	                    }
                    }else {
                    	 for (int i = 1; i <= 3; i++) {
 	                        String[] indicesValues = values[i].split("/");
 	                        int vertexIndexValue = Integer.parseInt(indicesValues[0]) - 1;
 	                        indices[indexIndex++] = vertexIndexValue;
 	                        normals[vertexIndexValue] = normalsTemp[Integer.parseInt(indicesValues[2])-1];
 	                    }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for(int i = 0; i < normals.length; i++) {
        	if(normals[i] == null)
        		normals[i] = new Vector3D(0,0,1);
        }

 
	}

	
	
	
}
