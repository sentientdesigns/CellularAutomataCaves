import processing.core.*;

import utils.Matrix2D;

import java.util.Random;

public class RandomCaves extends PApplet {
	int sizeX = 60;
	int sizeY = 60;
	
	int edgeX = 2;
	int edgeY = 2;
	
	int imageX = 600;
	int imageY = 600;
	
	PApplet p;
	
	Random r;
	float[][] matrix;
	
        public void setup () {
            size(imageX, imageY);
            background(100);
            
            r = new Random();
            matrix = Matrix2D.initialize(sizeX, sizeY, 0.f);
        } 

        public void keyTyped(){
            if(key=='r'){ matrix = initNoise(matrix); }
            if(key=='m'){ matrix = applyCellularAutomata(matrix); }
            if(key=='n'){ matrix = applyVonNeumannCA(matrix); }
        }
        
        public void draw(){
            this.image(render(matrix),0,0);
        }
		
	public float[][] initNoise(float[][] matrix){
		//beginDraw();
		float[][] result = Matrix2D.initialize(matrix.length, matrix[0].length, 0.f);
		for(int x=edgeX;x<result.length-edgeX;x++){
			for(int y=edgeY;y<result[0].length-edgeY;y++){
				result[x][y] = r.nextInt()>=0.5 ? 0 : 1.f;
			}
		}
		return result;
	}
        
        public float[][] applyVonNeumannCA(float[][] matrix){
		Random r = new Random();
		float[][] result = Matrix2D.initialize(matrix.length, matrix[0].length, 0.f);
		int width = matrix.length;
		int height = matrix[0].length;
			
		for(int x=0;x<width;x++){
		  for(int y=0;y<height;y++){
			int blackCount = 0;
			// VON NEUMAN
			for(int i=x-1;i<=x+1;i++){
                            for(int j=y-1;j<=y+1;j++){
                                if((i==x || j==y)){
                                    if(i<0||i>=width||j<0||j>=height){ 
                                      //blackCount++; 
                                    } else {
                                        if(matrix[i][j]>0.5f){
                                            blackCount++;
                                        }
                                    }
                                }
                            }
			}
			result[x][y]=(blackCount>=3) ? 1.f : 0.f;
		  }
		}
		return result;
	}
        
	public float[][] applyCellularAutomata(float[][] matrix){
		Random r = new Random();
		float[][] result = Matrix2D.initialize(matrix.length, matrix[0].length, 0.f);
		int width = matrix.length;
		int height = matrix[0].length;
			
		for(int x=0;x<width;x++){
		  for(int y=0;y<height;y++){
			int blackCount = 0;
			// VON NEUMAN
			for(int i=x-1;i<=x+1;i++){
			  for(int j=y-1;j<=y+1;j++){
				if(i<0||i>=width||j<0||j>=height){ 
				  //blackCount++; 
				} else {
				  if(matrix[i][j]>0.5f){
					blackCount++;
				  }
				}
                            }
			}
			result[x][y]=(blackCount>=5) ? 1.f : 0.f;
		  }
		}
		return result;
	}
		
	public float[][] applyInteriorCA(float[][] matrix){
		Random r = new Random();
		float[][] result = Matrix2D.copy(matrix);
		int width = matrix.length;
		int height = matrix[0].length;
			
		for(int x=0;x<width;x++){
		  for(int y=0;y<height;y++){
			int blackCount = 0;
			// VON NEUMAN
			for(int i=x-1;i<=x+1;i++){
			  for(int j=y-1;j<=y+1;j++){
				if(i<0||i>=width||j<0||j>=height){ 
				  //blackCount++; 
				} else {
				  if(matrix[i][j]>0.5f){
					blackCount++;
				  }
				}
			  }
			}
			if(blackCount==9){ result[x][y] = 0.5f; }
		  }
		}
		return result;
	}
	public float[][] applyShadingCA(float[][] matrix){
		Random r = new Random();
		float[][] result = Matrix2D.copy(matrix);
		int width = matrix.length;
		int height = matrix[0].length;
			
		for(int x=0;x<width;x++){
		  for(int y=0;y<height;y++){
			int blackCount = 0;
			float shade = 0;
			// VON NEUMAN
			for(int i=x-1;i<=x+1;i++){
			  for(int j=y-1;j<=y+1;j++){
				if(i<0||i>=width||j<0||j>=height){ 
				  //blackCount++; 
				} else {
				  if(matrix[i][j]>0.2f){
					blackCount++;
					shade+=matrix[i][j];
				  }
				}
			  }
			}
			if(blackCount==9){ result[x][y]= shade/9.f; }
		  }
		}
		return result;
	}
	
	public PImage render(float[][] matrix){
		int[] gridSize = { imageX/matrix.length, imageY/matrix[0].length };
		PGraphics pg = createGraphics(imageX, imageY, P2D);
		pg.background(this.color(255));
		for(int x=0;x<matrix.length;x++){
			for(int y=0;y<matrix[0].length;y++){
				for(int i=0;i<gridSize[0];i++){
					for(int j=0;j<gridSize[1];j++){
						int localX = x*gridSize[0]+j;
						int localY = y*gridSize[1]+i;
						pg.set(localX,localY,color((matrix[x][y])*255));	
					}
				}
			}
		}
		PImage img = pg.get();
		return img;
	}
	
        
        
	public static void main(String[] args) {
		PApplet.main( new String[]{"RandomCaves"} ); 
	}
}