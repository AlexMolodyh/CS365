/*
 * Copyright (C) 2018 AndreJPlath.com to Present.
 * Last Modified 02/03/2018 2:42:20 PM. All rights reserved. 
 *
 * @author Andre J Plath
 * @version 1.0 Lab_2, 02/03/2018
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class for CS362 Algorithms Lab 2
 *
 */
public class Lab_2
{
	public static void main(String[] args) throws IOException
	{
		Lab_2 labImplementation = new Lab_2();// New lab object
		int[] p = new int[] {30, 4, 8, 5, 10, 25, 15}; // p array


		// m array for DP version of MCM algorithm for p being < 30, 4, 8, 5, 10, 25, 15>. 
		Object[] dp_MCM = labImplementation.dynamicProgramingMCM(p);  
		int [] [] m = (int [] []) dp_MCM[0];
		int [] [] s = (int [] []) dp_MCM[1];

		// Print DP version of MCM algorithm
		System.out.println("Dynamic Programming version of MCM Algorithm");
		labImplementation.printMatrix(m);

		// m array for memoization version of MCM algorithm algorithm for p being < 30, 4, 8, 5, 10, 25, 15>. 
		m = (int [] []) labImplementation.memoizationMCM(p)[1];

		// Print memoization version of MCM algorithm algorithm
		System.out.println("Memoization Version of MCM Algorithm");
		labImplementation.printMatrix(m);


		// Reused code from Lab_1
		String pathToDataFile = "lab1_data.txt"; // Location of the lab data file
		long searchTime;
		int startingNumber = 1000;

		for (; startingNumber <= 10000000; startingNumber *= 10)
		{
			searchTime = System.nanoTime(); // Start nanoTime
			// Read data for decreasing order
			int[] elements = labImplementation.ReadTestData(pathToDataFile, startingNumber); 
			int[] topTenElements = labImplementation.largestTenElements(elements); // Top ten elements
			System.out.println("Ten Largest Elements of an Array of " + startingNumber); // Title print statement
			labImplementation.printArray(topTenElements); // Print array
			searchTime = System.nanoTime() - searchTime; // End time
			System.out.println("\nSearch time for ten largest elements (mS): " + (searchTime)/1000000);// mS output and formatting
			System.out.println();
			
			// Read data for decreasing sorting
			searchTime = System.nanoTime(); // Start nanoTime
			elements = labImplementation.ReadTestData(pathToDataFile, startingNumber);
			int[] testTop = labImplementation.sortedLargestTenElements(elements, startingNumber); //Sort elements using sort from lab 1
			System.out.println("Resorted Ten Largest Elements of an Array of " + startingNumber);// Top resorted elements
			labImplementation.printArray(testTop);// Print array
			searchTime = System.nanoTime() - searchTime; // End time
			System.out.println("\nSearch time for sorted elements (mS): " + (searchTime)/1000000);// mS output and formatting
			System.out.println();
			
		}
		
	}


	/**
	 * Method for implementing the Dynamic Programming version of MCM (Matrix Chain Multiplication).
	 * method was derived from Intro to Algorithms text book page 375. -1 was used to replace the
	 * infinity symbol.
	 * 
	 */
	public Object[] dynamicProgramingMCM (int[] p) 
	{
		int n = p.length - 1;
		int[][] m = new int[n+1][n+1];
		int[][] s = new int[n+1][n+1];

		for (int i = 1; i <= n; i++)
		{
			m[i][i] = 0;
		}
		for (int l = 2; l <= n; l++)
		{
			for (int i = 1; i <= (n - l + 1); i++)
			{
				int j = i + l - 1;
				m[i][j] = -1;
				for (int k = i; k <= (j - 1); k++)
				{
					int q = m[i][k] + m[k+1][j] + p[i-1] * p[k] * p[j];
					if (m[i][j] == -1)
					{
						m[i][j] = q;
						s[i][j] = k;
					}
					else if (q < m[i][j])
					{
						m[i][j] = q;
						s[i][j] = k;
					}
				}
			}
		}
		return new Object[] {m,s};
	}

	/**
	 * Method for implementing the Memoization version of MCM (Matrix Chain Multiplication).
	 * method was derived from Intro to Algorithms text book page 388. -1 was used to replace the
	 * infinity symbol.
	 */
	public Object[] memoizationMCM (int[] p)
	{
		int n = p.length-1;
		int [] [] m = new int [n+1] [n+1];

		for(int i = 1; i <= n; i++)
		{
			for (int j = 0; j <= n; j++)
			{
				m[i][j] = -1;

			}
		}
		return lookUpChain(m,p,1,n);
	}

	/**
	 * private helper method was derived from Intro to Algorithms text book page 388. 
	 * infinity symbol replaced with -1.
	 */
	private Object[] lookUpChain(int [] [] m, int[]p, int i, int j)
	{
		if (m[i][j] > -1)
		{
			return new Object[] {m[i][j], m};
		}
		else if (i == j)
		{
			m[i][j] = 0;
		}
		else
		{
			for (int k = i; k <= (j - 1); k++)
			{
				int x = (int) lookUpChain(m, p, i, k)[0];
				int y = (int) lookUpChain(m, p, k+1, j)[0];
				int q = x + y + p[i-1] * p[k] * p[j];
				if (m[i][j] == -1)
				{
					m[i][j] = q;
				}
				else if (q < m[i][j])
				{
					m[i][j] = q;
				}
			}
		}
		return new Object [] {m[i][j], m};
	}

	/**
	 * Method for printing matrices. Reused code found on Stackoverflow.
	 * Link for method https://stackoverflow.com/questions/5061912/printing-out-a-2-d-array-in-matrix-format
	 * 
	 */
	public void printMatrix(int[] [] matrix)
	{
		try
		{
			for(int i = 0; i < matrix.length; i++)
			{
				if(i > 0)
				{
					for(int j = 0; j < matrix.length; j++) 
					{
						//The following string and char[] will help keep the distance between
						// a cell with a zero in it and a cell with large values in it
						String number = "" + matrix[i][j];
						char[] space = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
						for(int s = 0; s < number.length(); s++)
							space[s] = number.charAt(s);
						String stringSpace = new String(space);
						// Makes sure we are not printing the 0 index column
						if(j > 0)
							System.out.print(stringSpace);
					}
					System.out.println();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Matrix is empty!!");
		}

	}
	

		/**
		 * Method for printing an array.
		 * @param array
		 */
		public void printArray(int[] array)
		{
			int i = 1;
			for ( int num : array)
			{
				System.out.print(num + ", ");
				i++;
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////LAB_1 ELEMENTS///////////////////////////////////////////////////
		/**
		 * Method to read provided test data from data file
		 * and input into an array. Modification of method was necessary. Code inspired and reused.
		 * https://stackoverflow.com/questions/13716830/reading-ints-from-a-txt-file-and-storing-to-an-array
		 * @param array The initial unsorted array .
		 * @throws IOException if array contains items.
		 */
		public int[] ReadTestData(String pathToDataFile, int n) throws IOException
		{
			int[] data = new int[n];
			BufferedReader reader = new BufferedReader(new FileReader(pathToDataFile));
			String valuesOfDataFile = null;

			int i = 0;
			while ((valuesOfDataFile = reader.readLine()) != null && i < n)
			{
				data[i] = Integer.parseInt(valuesOfDataFile);
				i++;
			}
			reader.close();
			//System.out.println("Data sucessfully passed to array!"); //Annoying
			return data;
		}
		
//		public void auxMergeSort (int[] array, int startIndex, int endIndex)
//		{
//			int n = endIndex - startIndex;
//			if (n <= 1)
//			{
//				return;
//			}
//			int midIndex = startIndex + n/2;
//			auxMergeSort (array, startIndex, midIndex);
//			auxMergeSort (array, midIndex, endIndex);
//			merge(array, n, startIndex, midIndex, endIndex);
//		}
//		private void merge (int[] array, int n, int startIndex, int midIndex, int endIndex)
//		{
//			int[] temp = new int[n];
//			int i = startIndex;
//			int j = midIndex;
//			for (int k = 0; k < n; k++)
//			{
//				if (i == midIndex)
//				{
//					temp[k] = array[j++];
//				}
//				else if (j == endIndex)
//				{
//					temp[k] = array[i++];
//				}
//				else if (array[j] < array[i])
//				{
//					temp[k] = array[j++];
//				}
//				else
//				{
//					temp[k] = array[i++];
//				}
//			}
//			for (int k = 0; k < n; k++)
//			{
//				array[startIndex + k] = temp[k];
//			}
//		}
//		public void swap(int[] array, int i, int j)
//		{
//			int temp = array[j];
//			array[j] = array[i];
//			array[i] = temp;	
//		}
		
		/**
		 * Method that sorts elements, using recursion, between the startIndex and endIndex 
		 * using Quick Sort with the pivot to be the average of the values. Some code used or modified 
		 * from https://www.geeksforgeeks.org/quick-sort/
		 * 
		 * @param array The initial unsorted array.
		 * @param startIndex The starting index of the array.
		 * @param endIndex The ending index of the array.
		 */
		public void auxQuickSort(int array[], int startIndex, int endIndex)
		{
			if(startIndex < endIndex)
			{
				//partitioningIndex is partitioning index
				int partitioningIndex = partition(array, startIndex, endIndex);

				auxQuickSort(array, startIndex, partitioningIndex -1);
				auxQuickSort(array, partitioningIndex + 1, endIndex);
			}
		}

		/**
		 * Method that takes element average as pivot for sorting.
		 * 
		 * @param array The initial unsorted array.
		 * @param startIndex The starting index of the array.
		 * @param endIndex The ending index of the array.
		 */
		private int partition(int array[], int startIndex, int endIndex)
		{
			int mid = (startIndex + endIndex)/2;
			int pivot = (startIndex + mid + endIndex)/3; //Get pivot from average of startIndex, mid, and endIndex

			swap(array, pivot, endIndex);	//Switch the pivot element with the last element to get pivot in place

			int x = array[endIndex];
			int i = startIndex - 1;
			for (int j = startIndex; j <= (endIndex -1); j++)
			{
				if (array[j] <= x)
				{
					i++;
					swap(array, i, j);
				}
			}
			swap(array, (i+1), endIndex);
			return i + 1;
		}

		public void swap(int[] array, int i, int j)
		{
			int tempArray = array[j];
			array[j] = array[i];
			array[i] = tempArray;	
		}


		///////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////END LAB_1/////////////////////////////////////////////////
		/**
		 * Method to list the largest 10 elements of the data you read, 
		 * and listing them in decreasing order as the output. 
		 * https://stackoverflow.com/questions/32395648/largest-5-in-array-of-10-numbers-without-sorting
		 * 
		 */
		public int[] largestTenElements(int[] elements)
		{
			int[] largest = new int[10];

			return largestTenElementsRecursive(elements, largest, 0);
		}

		private int[] largestTenElementsRecursive(int[] elements, int [] largest, int i)
		{
			if (i == 10)
			{
				return largest;
			}
			int k = getLargest(elements);
			largest[i] = elements[k];
			elements[k] = 0;
			return largestTenElementsRecursive(elements, largest, i+1);
		}

		/**
		 * Method to get largest int in an array. 
		 * https://stackoverflow.com/questions/32395648/largest-5-in-array-of-10-numbers-without-sorting
		 */
		public int getLargest(int[] array) 
		{
			int max = 0;
			int k = 0;

			for(int i = 0;i<array.length;i++)
			{
				int num = array[i];
				if (num > max)
				{
					max = num;
					k = i;
				}
			}	
			return k;
		}
		
		public int[] sortedLargestTenElements(int[] elements, int n)
		{
			int [] largest = new int[10];
			auxQuickSort(elements, 0, n - 1);
			reverseArray(elements, n);
			
			for (int i = 0; i < 10; i++)
			{
				largest[i] = elements[i];
			}
			return largest;
		}
		
		public void reverseArray(int[] array, int n)
		{
			for(int x = 0; x < n/2; x++)
			{
				int a = x;
				int z = n - 1 - x;
				swap(array,a, z);
			}
		}

	}	

