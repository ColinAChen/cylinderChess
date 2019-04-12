import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class NeuralNet
{
	private int numLayers;
	private int[] layerSizes;
	private double[][][] weights;
	private double[][] biases;

	// construct neural net based on existing file
	public NeuralNet()		
	{
		loadFromFile();
	}

	// construct untrained neural net with given layer arcitechture
	public NeuralNet(int[] hiddenLayers)
	{
		numLayers = hiddenLayers.length + 2;
		layerSizes = new int[numLayers];
		weights = new double[numLayers - 1][][];
		biases = new double[numLayers - 1][];
		
		// initialize layer sizes
		layerSizes[0] = 65;
		layerSizes[numLayers - 1] = 1;
		for(int i = 0; i < hiddenLayers.length; i++)
			layerSizes[i + 1] = hiddenLayers[i];
	
		// create weight layers
		for(int i = 0; i < weights.length; i++)
			weights[i] = new double[layerSizes[i + 1]][layerSizes[i]];
				
		// create bias layers
		for(int i = 0; i < biases.length; i++)
			biases[i] = new double[layerSizes[i + 1]];

		// populate weights and biases randomly with gaussian distribution
		Random randNum = new Random();

		for(int i = 0; i < weights.length; i++)
		{
			for(int j = 0; j < weights[i].length; j++)
			{
				for(int k = 0; k < weights[i][j].length; k++)
					weights[i][j][k] = randNum.nextGaussian();
			}
		}
		
		for(int i = 0; i < biases.length; i++)
		{
			for(int j = 0; j < biases[i].length; j++)
				biases[i][j] = randNum.nextGaussian();
		}	
	}

	public double evaluateNet(double[][][] testData)
	{
		double avgError;
		double sumError = 0;
		for(int i = 0; i < testData.length; i++)
		{
			double eval = activateLayers(testData[i][0])[0];
			double trueEval = testData[i][1][0];
			double error = Math.abs(trueEval - eval);
			sumError += error;
		}
		avgError = sumError / testData.length;
		return avgError;
	}

	public void stochasticGradientDescent(double[][][] trainingData, int miniBatchSize, double eta, int numEpochs)
	{
		shuffle(trainingData);
		for(int i = 0; i < trainingData.length; i += miniBatchSize)
		{
			for(int j = i; j < i + miniBatchSize && j < trainingData.length; j++)
				backpropagation(trainingData[j], miniBatchSize, eta);
		}

		if(numEpochs > 1)
			stochasticGradientDescent(trainingData, miniBatchSize, eta, numEpochs - 1);
	}

	// trainingSample[0] is input vector, trainingSample[1] is corresponding idealOutput
	public void backpropagation(double[][] trainingSample, int miniBatchSize, double eta)
	{
		double[][] activations = new double[numLayers][];
		double[][] zValues = new double[numLayers - 1][];

		double[] activated = new double[trainingSample[0].length];
		for(int i = 0; i < trainingSample[0].length; i++)
			activated[i] = trainingSample[0][i]; 
 
		activations[0] = new double[activated.length];
		for(int i = 0; i < activated.length; i++)
			activations[0][i] = activated[i];

		// forward propagate
		for(int layer = 0; layer < numLayers - 1; layer++)
		{
			// calculate z-values
			// dot input with weights
			activated = matrixMultiply(weights[layer], activated);
		
			// add biases
			for(int i = 0; i < activated.length; i++)
				activated[i] += biases[layer][i];

			zValues[layer] = new double[activated.length];
			for(int i = 0; i < activated.length; i++)
				zValues[layer][i] = activated[i];
		
			// apply sigmoid function
			for(int i = 0; i < activated.length; i++)
				activated[i] = sigmoid(activated[i]);

			activations[layer + 1] = new double[activated.length];
			for(int i = 0; i < activated.length; i++)
				activations[layer + 1][i] = activated[i];
		}

		// backpropagate
		// calculate sigmoid prime for zValues
		for(int r = 0; r < zValues.length; r++)
		{
			for(int c = 0; c < zValues[r].length; c++)
				zValues[r][c] = sigmoidPrime(zValues[r][c]);
		}

		// calculate cost partials for output layer
		double[] costPartials = new double[activations[activations.length - 1].length];
		for(int i = 0; i < costPartials.length; i++)
			costPartials[i] = activations[activations.length - 1][i];
		
		for(int i = 0; i < costPartials.length; i++)
			costPartials[i] = costPartial(costPartials[i], trainingSample[1][0]);

		// create error matrix to store error for each layer
		double[][] error = new double[numLayers - 1][];

		// calculate error for output layer
		error[error.length - 1] = hadamardProduct(costPartials, zValues[zValues.length - 1]);

		// calculate error for prior layers
		for(int layer = error.length - 2; layer >= 0; layer--)
		{
			double[] multiplied = matrixMultiply(transpose(weights[layer + 1]), error[layer + 1]);
			error[layer] = hadamardProduct(multiplied, zValues[layer]);
		}

		// update weights and biases
		for(int i = 0; i < error.length; i++)
		{
			for(int j = 0; j < error[i].length; j++)
			{
				// update biases
				biases[i][j] -= (eta / miniBatchSize) * error[i][j];
				
				// update weights
				for(int k = 0; k < weights[i][j].length; k++)
				{
					double weightPartial = error[i][j] * activations[i][k];	
					weights[i][j][k] -= (eta / miniBatchSize) * weightPartial;
				}
			}
		}		
	}

	public void shuffle(double[][][] arr)
	{
		Random randNum = new Random();
		for(int i = arr.length - 1; i > 0; i--)
		{
			int swapIndex = randNum.nextInt(i);
			double[][] temp = arr[swapIndex];
			arr[swapIndex] = arr[i];
			arr[i] = temp;
		}
	}

	public double[] activateLayers(double[] input)
	{
		return activateHelper(input, 0);
	}

	public double[] activateHelper(double[] input, int layer)
	{
		// check if last layer
		if(layer == numLayers - 1)
			return input;
		
		// dot input with weights
		double[] activated = matrixMultiply(weights[layer], input);
		// add biases
		for(int i = 0; i < activated.length; i++)
			activated[i] += biases[layer][i];
		
		// apply sigmoid function
		for(int i = 0; i < activated.length; i++)
			activated[i] = sigmoid(activated[i]);
		
		return activateHelper(activated, layer + 1);
	}

	public double dotProduct(double[] first, double[] second)
	{
		double result = 0;
		for(int i = 0; i < first.length; i++)
			result += first[i] * second[i];

		return result;
	}

	// hadamard of two N x 1 matrices, where N = first.length = second.length
	public double[] hadamardProduct(double[] first, double[] second)
	{
		double[] result = new double[first.length];
		for(int i = 0; i < first.length; i++)
			result[i] = first[i] * second[i];
	
		return result;
	}

	public double[][] hadamardProduct(double[][] first, double[][] second)
	{
		double[][] result = new double[first.length][first[0].length];
		for(int r = 0; r < first.length; r++)
		{
			for(int c = 0; c < first[0].length; c++)
				result[r][c] = first[r][c] * second[r][c];
		}
		
		return result;
	}

	// M x N matrix * N x 1 matrix, where N = second.length
	public double[] matrixMultiply(double[][] first, double[] second)
	{
		// convert second to 2D matrix
		double[][] second2D = transpose(second);

		// multiply matrices
		double[][] result2D = matrixMultiply(first, second2D);

		// convert result to 1D matrix
		double[] result = new double[result2D.length];
		for(int i = 0; i < result2D.length; i++)
			result[i] = result2D[i][0];

		return result;
	}

	public double[][] matrixMultiply(double[][] first, double[][] second)
	{
		double[][] result = new double[first.length][second[0].length];
		for(int rowF = 0; rowF < first.length; rowF++)	
		{
			for(int colF = 0; colF < first[rowF].length; colF++)
			{
				for(int colS = 0; colS < second[0].length; colS++)
				{
					double value = 0;
					for(int rowS = 0; rowS < second.length; rowS++)
						value += first[rowF][colF] * second[rowS][colS]; 
					
					result[rowF][colS] = value;
				}
			}
		}

		return result;
	}		

	public double[][] transpose(double[] matrix)
	{
		double[][] result = new double[matrix.length][1];
		for(int i = 0; i < matrix.length; i++)
			result[i][0] = matrix[i];

		return result;
	}	

	public double[][] transpose(double[][] matrix)
	{
		double[][] result = new double[matrix[0].length][matrix.length];
		for(int r = 0; r < matrix.length; r++)
		{
			for(int c = 0; c < matrix[0].length; c++)
				result[c][r] = matrix[r][c];
		}
		
		return result;
	}

	// partial derivative of quadratic cost with respect to output activations
	public double costPartial(double activation, double idealOutput)
	{
		return activation - idealOutput;
	}

	public double sigmoid(double z)
	{
		return 1 / (1 + Math.pow(Math.E, z * -1));
	}

	public double sigmoidPrime(double z)
	{
		return sigmoid(z) * (1 - sigmoid(z));
	}

	public void recordToFile()
	{
		File file = new File("netParameters1.txt");
		try
		{
			if(!file.createNewFile())
			{
				file.delete();
				file.createNewFile();
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			// write layers
			writer.write("Layers");
			writer.newLine();
			for(int i = 0; i < layerSizes.length; i++)
			{
				writer.write(Integer.toString(layerSizes[i]));
				writer.newLine();
			}
			writer.newLine();

			// write weights
			writer.write("Weights");
			writer.newLine();	
			for(int i = 0; i < weights.length; i++)
			{	
				writer.write("Layer " + Integer.toString(i));
				writer.newLine();
				for(int j = 0; j < weights[i].length; j++)
				{
					for(int k = 0; k < weights[i][j].length; k++)
					{
						writer.write(Double.toString(weights[i][j][k]));
						writer.newLine();
					}
					writer.newLine();
						
				}
				writer.newLine();
			}
	
			writer.write("Biases");
			writer.newLine();
			for(int i = 0; i < biases.length; i++)
			{
				writer.write("Layer " + Integer.toString(i));
				writer.newLine();
				for(int j = 0;j < biases[i].length; j++)
				{
					writer.write(Double.toString(biases[i][j]));
					writer.newLine();
				}
				writer.newLine();
			}
				
			writer.write("END");
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			System.out.println("recordToFile IOException");
		}
	}

	public void loadFromFile()
	{	
		final String NEW_LINE = System.getProperty("line.separator");
		File file = new File("netParameters1.txt");
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int mode = 0;
			
			for(;;)
			{
				String nextLine = reader.readLine();
			
				if(nextLine.equals("END"))
					break;
				else if(nextLine.equals("Layers"))
					mode = 1;
				else if(nextLine.equals("Weights"))
					mode = 2;
				else if(nextLine.equals("Biases"))
					mode = 3;

				switch(mode)
				{
				// layerSizes
				case 1:
					ArrayList<Integer> sizes = new ArrayList<Integer>();

					nextLine = reader.readLine();
					while(!nextLine.equals(""))
					{
						sizes.add(Integer.parseInt(nextLine));
						nextLine = reader.readLine();
					}
				
					// initialize layerSizes
					numLayers = sizes.size();
					layerSizes = new int[numLayers];
					for(int i = 0; i < sizes.size(); i++)
						layerSizes[i] = sizes.get(i);

					// create weights and biases arrays
					weights = new double[numLayers - 1][][];
					for(int i = 0; i < weights.length; i++)
						weights[i] = new double[layerSizes[i + 1]][layerSizes[i]];
				
					biases = new double[numLayers - 1][];
					for(int i = 0; i < biases.length; i++)
						biases[i] = new double[layerSizes[i + 1]];
					
					break;
				// weights
				case 2:
					int weightLayerCount = 0;
					while(weightLayerCount < weights.length)
					{
						nextLine = reader.readLine();
						nextLine = reader.readLine();

						int row = 0; 
						while(!nextLine.equals(""))
						{
							int col = 0;
							while(!nextLine.equals(""))
							{
								weights[weightLayerCount][row][col] = Double.parseDouble(nextLine);
								col++;
								nextLine = reader.readLine();
							}
							
							row++;	
							nextLine = reader.readLine();
						}

						weightLayerCount++;
					}
					break;
				// biases
				case 3:
					int biasLayerCount = 0;
					while(biasLayerCount < biases.length)
					{
						nextLine = reader.readLine();
						nextLine = reader.readLine();

						int index = 0;
						while(!nextLine.equals(""))
						{
							biases[biasLayerCount][index] = Double.parseDouble(nextLine);
							index++;
							nextLine = reader.readLine();
						}  

						biasLayerCount++;
					}
					break;
				default:
					continue;
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("loadFromFile IOException");
		}
	}

	public void printLayers()
	{
		System.out.println("Number of layers: " + numLayers);
		for(int i = 0; i < layerSizes.length; i++)
			System.out.print(layerSizes[i] + " ");
		
		System.out.println();
		for(int i = 0; i < weights.length; i++)
		{
			System.out.println("Layer: " + i);
			for(int j = 0; j < weights[i].length; j++)
			{
				for(int k = 0; k < weights[i][j].length; k++)
					System.out.print(weights[i][j][k] + " ");
			
				System.out.println();
			}
		}

		System.out.println();
		System.out.println("Biases: ");
		for(int i = 0; i < biases.length; i++)
		{
			for(int j = 0; j < biases[i].length; j++)
				System.out.println(biases[i][j]);
		}
	}		
}