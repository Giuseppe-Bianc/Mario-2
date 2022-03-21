package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import javax.print.DocFlavor;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

	private static final String QT = "'", ERR = "ERROR: ", ERR2 = ERR + QT;
	private static final String SPC = "'\n\t", LEND = "\r\n";
	private static final String SCF = "shader compilation failed.";
	private static final String TYP = "#type", VRT = "vertex", FRG = "fragment";
	private static final String RNG = "a-z", UNT = "Unexpected token '";
	private static final boolean NRM = false;
	private int shaderProgramID;
	private boolean beingUsed = NRM;

	private String vertexSource;
	private String fragmentSource;
	private final String filepath;


	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(" + TYP + ")( )+([" + RNG + RNG.toUpperCase() + "]+)");

			int index = source.indexOf(TYP) + 6;
			int eol = source.indexOf(LEND, index);
			String firstPattern = source.substring(index, eol).trim();

			index = source.indexOf(TYP, eol) + 6;
			eol = source.indexOf(LEND, index);
			String secondPattern = source.substring(index, eol).trim();

			if (firstPattern.equals(VRT)) {
				vertexSource = splitString[1];
			} else if (firstPattern.equals(FRG)) {
				fragmentSource = splitString[1];
			} else {
				throw new IOException(UNT + firstPattern + QT);
			}

			if (secondPattern.equals(VRT)) {
				vertexSource = splitString[2];
			} else if (secondPattern.equals(FRG)) {
				fragmentSource = splitString[2];
			} else {
				throw new IOException(UNT + secondPattern + QT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			assert NRM : ERR + "Could not open file for shader: '" + filepath + QT;
		}
	}

	public void compile() {
		int vertexID, fragmentID;

		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);

		int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println(ERR2 + filepath + SPC + VRT + SCF);
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert NRM : "";
		}

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);

		success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if (success == GL_FALSE) {
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println(ERR2 + filepath + SPC + FRG + SCF);
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert NRM : "";
		}

		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);

		success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		if (success == GL_FALSE) {
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println(ERR2 + filepath + SPC + "Linking of shaders failed.");
			System.out.println(glGetProgramInfoLog(shaderProgramID, len));
			assert NRM : "";
		}
	}

	/**
	 * If the shader is not being used, then use it
	 */
	public void use() {
		if (!beingUsed) {
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}

	/**
	 * Detach the shader program from the OpenGL pipeline
	 */
	public void detach() {
		glUseProgram(0);
		beingUsed = NRM;
	}

	/**
	 * It takes a variable name and a matrix, and uploads the matrix to the GPU
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param mat4    The matrix to upload.
	 */
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(varLocation, NRM, matBuffer);
	}

	/**
	 * *Uploads a 3x3 matrix to the shader program.*
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param mat3    The matrix to upload.
	 */
	public void uploadMat3f(String varName, Matrix3f mat3) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);
		glUniformMatrix3fv(varLocation, NRM, matBuffer);
	}

	/**
	 * Uploads a Vector4f to the shader
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param vec     The vector to upload.
	 */
	public void uploadVec4f(String varName, Vector4f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
	}

	/**
	 * Uploads a Vector3f to a uniform variable in the shader
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param vec     The vector to upload.
	 */
	public void uploadVec3f(String varName, Vector3f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform3f(varLocation, vec.x, vec.y, vec.z);
	}

	/**
	 * Uploads a Vector2f to a uniform variable in the shader
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param vec     The vector to upload.
	 */
	public void uploadVec2f(String varName, Vector2f vec) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform2f(varLocation, vec.x, vec.y);
	}

	/**
	 * Uploads a float value to a uniform variable in the shader program
	 *
	 * @param varName The name of the variable to be uploaded.
	 * @param val     The value to be uploaded.
	 */
	public void uploadFloat(String varName, float val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1f(varLocation, val);
	}

	/**
	 * This function uploads an integer value to the specified uniform variable in the shader
	 * program
	 *
	 * @param varName The name of the variable to be uploaded.
	 * @param val     The value to be uploaded.
	 */
	public void uploadInt(String varName, int val) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, val);
	}

	/**
	 * Uploads a texture to the GPU
	 *
	 * @param varName The name of the variable in the shader program.
	 * @param slot    The texture slot to bind the texture to.
	 */
	public void uploadTexture(String varName, int slot) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1i(varLocation, slot);
	}

	/**
	 * It uploads an array of integers to the GPU.
	 *
	 * @param varName The name of the variable to be uploaded.
	 * @param array   The array to be uploaded.
	 */
	public void uploadIntArray(String varName, int[] array) {
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		use();
		glUniform1iv(varLocation, array);
	}
}
