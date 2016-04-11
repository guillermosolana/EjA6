package eja6;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import eja6.graphics.Shader;
import eja6.input.Input;
import eja6.level.Level;
import eja6.maths.Matrix4f;

/**
 * width/height =  
 * 		1280/ x = 10
		128 = x 
		
		720 / y = 10
		72 = y
		
		x = y ?  ----  x = y * e -----  x/y * e ------ e = 1.777777

 *  
 * @author guillermo
 *
 */
public class Main implements Runnable {

	private int width = 1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running = false;
	
	private long window;
	private Input input = new Input();
	
	private Level level;
	
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init(){
		if (glfwInit() != GL_TRUE) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Ejercicio A6", NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		

		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);		
		glfwSetKeyCallback(window, input);	
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		//glClearColor(1.0f,1.0f,1.0f,1.0f);
		
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-5.0f * width/height, 5.0f * width/height , -5.0f, 5.0f, -1.0f, 1.0f);
		Shader.PACMAN.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.PACMAN.setUniform1i("tex", 1);
		
		
		Shader.ROCK.setUniformMat4f("pr_matrix", pr_matrix);
		Shader.ROCK.setUniform1i("tex", 1);

		
		level = new Level();
	}
	
	@Override
	public void run() {
		init();
		while (running) {
			update();
			render();
			if (glfwWindowShouldClose(window) == GL_TRUE)
				running = false;
		}
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	public void update(){
		glfwPollEvents();
		level.update();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		
		int error = glGetError();
		if (error != GL_NO_ERROR)
			System.out.println(error);
		
		glfwSwapBuffers(window);
	}
	
	public static void main(String[] args) {
		new Main().start();
	}

	

}
