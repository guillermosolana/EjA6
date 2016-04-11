package eja6.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eja6.graphics.Shader;
import eja6.level.Pacman;
import eja6.maths.Matrix4f;
import eja6.maths.Vector3f;


public class Level {
	
	private Pacman pacman;
	//private int rocksNumber = 18; y10
	private final int fHor = 18;
	private final int fVer = 10;
	private final int randCol = 5;
	private final int randRow = 8;
	private final int rocksNumber = fHor*2 + fVer*2 + randCol +  randRow;
	
	private Rock[] rocks = new Rock[rocksNumber];
	private Random rand = new Random();
	
	

	public Level() {		
		createRocks();
		pacman = new Pacman();
	}
	
	public void update() {
		updateRocks();
		pacman.update();
		if(collision()){
			pacman.stop(pacman.getUltimaKey());
		}	
	}
	
	public void render() {
		pacman.render();
		renderRocks();
	}
	
	private boolean collision() {
		for (int i = 0; i < rocksNumber; i++) {
			float px = pacman.getX();
			float py = pacman.getY();
			float rx = rocks[i].getX();
			float ry = rocks[i].getY();
			
			float px0 = px - pacman.getSize()/2;
			float px1 = px + pacman.getSize()/2;
			float py0 = py - pacman.getSize()/2;
			float py1 = py + pacman.getSize()/2;
			
			float rx0 = rx;
			float rx1 = rx + Rock.getWidth();
			float ry0 = ry;
			float ry1 = ry + Rock.getHeight();		
			
			if (px1 > rx0 && px0 < rx1) {
				
				if (py1 > ry0 && py0 < ry1) {
					System.out.println("colision");
					System.out.println("ultima tecla: "+ pacman.getUltimaKey());
					return true;
				}
			}
		}
		return false;
	}
	
	
	private void createRocks() {
		Rock.create();
		
		
//		for (int i = 0; i < rocksNumber; i++) {
//		
//			//Contorno de pantalla
//			if(i < fHor){
//				bS++;
//				float n = bS - 9.9f;
//				rocks[i] = new Rock(n, 3.7f);
//			}
//			if(i >= fHor && i < fHor+fHor){
//				bI++;
//				float n = bI - 9.9f;
//				rocks[i] = new Rock(n, -5.0f);
//			}
//			if(i >= fHor+fHor && i < fHor+fHor+fVer){
//				bR++;
//				float n = bR - 6.0f;
//				rocks[i] = new Rock(7.9f, n);
//			}
//			if(i >= fHor+fHor+fVer && i < fHor+fHor+fVer+fVer){
//				bL++;
//				float n = bL - 6.0f;
//				rocks[i] = new Rock(-8.9f, n);
//			}
//			
//			if(ramdomRocks){
//				
//			}
//			
//		}
		List<Rock> array = new ArrayList<Rock>();
		
		for(int i=0;i<fHor;i++){
			float n = i - 9.1f;
			array.add(new Rock(n, 3.7f));
		}
		for(int i=0;i<fHor;i++){
			float n = i - 9.1f;
			array.add(new Rock(n, -5.0f));
		}
		for(int i=0;i<fVer;i++){
			float n = i - 5.3f;
			array.add(new Rock(7.9f, n));
		}
		for(int i=0;i<fVer;i++){
			float n = i - 5.3f;
			array.add(new Rock(-8.9f, n));
		}
		
		float y = 0.0f;
		float x = 0.0f;
		int min = 0,max = 0;
		
		min=-6;
		max=-2;
		x = (rand.nextInt((max - min) + 1) + min) * 1.0f;
		for(int i=0;i<randCol;i++){
			y = i - 4.0f;
			array.add(new Rock(x, y));
		}
		
		
		min=-2;
		max=2;
		y = (rand.nextInt((max - min) + 1) + min) * 1.0f;
		for(int i=0;i<randRow;i++){
			x = i - 4.0f;
			array.add(new Rock(x, y));
		}
		
		
		
		
		//conversion
		int i = 0;
		for(Rock r : array){
			rocks[i] = r;
			i++;
		}
	}
	
	private void updateRocks() {
	}
	
	private void renderRocks() {
		Shader.ROCK.enable();
		Shader.ROCK.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(0.0f, 0.0f, 0.0f)));
		Rock.getTexture().bind();
		Rock.getMesh().bind();
		
		for (int i = 0; i < rocksNumber; i++) {
			Shader.ROCK.setUniformMat4f("ml_matrix", rocks[i].getModelMatrix());
			Rock.getMesh().draw();
		}
		Rock.getMesh().unbind();
		Rock.getTexture().unbind();
		
	}
}

