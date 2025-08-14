package com.itsallbinary;

import game.RockPaperScissors;
import gui.test.test.Test;
import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import raven.chat.component.ChatBox;
import raven.chat.model.ModelMessage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class OpenNLPChatBot {
	static Reply ob = new Reply();
	static jokes jk = new jokes();
	static PlayMusic music = new PlayMusic();
	static SolveMath mathsolver = new SolveMath();
	public static String answer = "";

	public static void sub(String message) throws FileNotFoundException, IOException, InterruptedException {

		// Train categorizer model to the training data we created.
		DoccatModel model = trainCategorizerModel();
		String userInput = message;
		String userTemp = userInput.toLowerCase();

		boolean conversationComplete = false;
		if(userTemp.contains("play music")||userTemp.contains("music")||userTemp.contains("play a song")||userTemp.contains("listen to a song")
				||userTemp.contains("next song")||userTemp.contains("play another song"))
		{
			answer = "Sure, Here's one for you!!";
			displayMessage(answer);
			music.PlayRandomMusic();
		} else if (userTemp.contains("open youtube") || userTemp.contains("open google")
		|| userTemp.contains("open instagram") || userTemp.contains("open facebook") || userTemp.contains("open gmail")) {
			try {
				if (userTemp.contains("open google")) {
					displayMessage("Opening google...");
					Desktop d = Desktop.getDesktop();
					try {
						d.browse(new URI("https://www.google.com/"));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				} else if (userTemp.contains("open youtube")) {
					displayMessage("Opening youtube...");
					Desktop d = Desktop.getDesktop();
					try {
						d.browse(new URI("https://www.youtube.com/"));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				} else if (userTemp.contains("open instagram")) {
					displayMessage("Opening instagram...");
					Desktop d = Desktop.getDesktop();
					try {
						d.browse(new URI("https://www.instagram.com/"));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				} else if (userTemp.contains("open facebook")) {
					displayMessage("Opening facebook...");
					Desktop d = Desktop.getDesktop();
					try {
						d.browse(new URI("https://www.facebook.com/"));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				} else if (userTemp.contains("open gmail")) {
					displayMessage("Opening gmail...");
					Desktop d = Desktop.getDesktop();
					try {
						d.browse(new URI("www.gmail.com"));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				}
			}
			catch (Exception ee)
			{
				displayMessage("Hmm seems like you are not connected to the internet");
			}
		}
		else if (userTemp.contains("yes i want to play a game")||userTemp.contains("game")||userTemp.contains("play a game"))
		{
			if(userTemp.contains("play another game")||userTemp.contains("another game"))
			{
				Desktop d = Desktop.getDesktop();
				try {
					d.browse(new URI("https://doodlecricket.github.io/#/"));
				} catch (URISyntaxException e) {
					throw new RuntimeException(e);
				}
			}
			else {
				answer = "I have 2 games on me\n" + "1.RockPaperScissors\n2.Changed my mind";
				displayMessage(answer);
				String ch = "1";
				if (ch.equalsIgnoreCase("1")) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							RockPaperScissors.sub();
						}
					});
				}
			}
		}
		else if(userTemp.contains("tell me a joke")||userTemp.contains("joke")||userTemp.contains("make me laugh")||userTemp.contains("tell a joke")) {answer = "HAHA Sure, here you go!!\n" + jk.Randomjoke();
			displayMessage(answer);

		}
		else if (userTemp.contains("today's date") || userTemp.contains("what day is it") || userTemp.contains("the date")) { // use contains() instead of																			// equalsIgnoreCase()
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDateTime now = LocalDateTime.now();
			answer = "Today's date is: " + dtf.format(now);
			displayMessage(answer);
		}
		// If user asks for current time
		else if (userTemp.contains("the time") || userTemp.contains("what time is it")) { // use contains() instead of																		// equalsIgnoreCase()
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			answer = "Time is: " + dtf.format(now);
			displayMessage(answer);
		}
		else
		{
                // Break users chat input into sentences using sentence detection.
			String[] sentences = breakSentences(userInput);
			conversationComplete = false;

			answer = "";

                // Loop through sentences.
			for (String sentence : sentences) {

                    // Separate words from each sentence using tokenizer.
				String[] tokens = tokenizeSentence(sentence);

                    // Tag separated words with POS tags to understand their gramatical structure.
				String[] posTags = detectPOSTags(tokens);
				boolean flag = false;
				for(String ele: posTags){
					if (ele.contains("CD"))
						flag = true;
				}
				if(flag)
				{
					String math_ans = mathsolver.sol_math(userInput);
					displayMessage(math_ans);
					break;
				}
				else {
					// Lemmatize each word so that its easy to categorize.
					String[] lemmas = lemmatizeTokens(tokens, posTags);

					// Determine BEST category using lemmatized tokens used a mode that we trained
					// at start.
					String category = detectCategory(model, lemmas);
					if(category.equalsIgnoreCase("not-under"))
					{
						try {
							try {
								URL url = new URL("https://google.co.in");
								URLConnection connection = url.openConnection();
								connection.connect();
								displayMessage("Here some results for you ...");
								java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://www.google.com/search?hl=en&q=" + message.replace(" ", "+") + "&btnG=Google+Search"));

							} catch (Exception ee) {
								displayMessage("Connect with internet connection for get better results...");
							}
						}
						catch(Exception eee)
						{
							answer = ob.Answer(category);
							displayMessage(answer);
						}
					}
					else
					{
						// Get predefined answer from given category & add to answer.
						answer = answer + ' '+ ob.Answer(category);
					}
					// If category conversation-complete, we will end chat conversation.
					if ("conversation-complete".equals(category)) {
							conversationComplete = true;
					}
				}
			}
			if(answer!=null)
				displayMessage(answer);
		}

		// Print answer back to user. If conversation is marked as complete, then end
		// loop & program.
		//System.out.println("##### Chat Bot: " + answer);
		if (conversationComplete)
		{
			System.exit(0);
		}


	}

	private static void displayMessage(String message) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		String date = df.format(new Date());
		ImageIcon icon = new ImageIcon("java/gui/test/test/p1.png");
		Test.chatArea.addChatBox(new ModelMessage(icon, "Buddy", date, message), ChatBox.BoxType.LEFT);
		Test.chatArea.clearTextAndGrabFocus();
	}

	/**
	 * Train categorizer model as per the category sample training data we created.
	 *
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
		// faq-categorizer.txt is a custom training data with categories as per our chat
		// requirements.
		InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(new File("faq-categorizer.txt"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);

		// Train a model with classifications from above file.
		DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		return model;
	}

	/**
	 * Detect category using given token. Use categorizer feature of Apache OpenNLP.
	 *
	 * @param model
	 * @param finalTokens
	 * @return
	 * @throws IOException
	 */
	private static String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {

		// Initialize document categorizer tool
		DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

		// Get best possible category.
		double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
		String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
		System.out.println("Category: " + category);

		return category;

	}

	/**
	 * Break data into sentences using sentence detection feature of Apache OpenNLP.
	 *
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] breakSentences(String data) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-sent.bin")) {

			SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));

			String[] sentences = myCategorizer.sentDetect(data);
			System.out.println("Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")));

			return sentences;
		}
	}

	/**
	 * Break sentence into words & punctuation marks using tokenizer feature of
	 * Apache OpenNLP.
	 *
	 * @param sentence
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-token.bin")) {

			// Initialize tokenizer tool
			TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));

			// Tokenize sentence.
			String[] tokens = myCategorizer.tokenize(sentence);
			System.out.println("Tokenizer : " + Arrays.stream(tokens).collect(Collectors.joining(" | ")));

			return tokens;

		}
	}

	/**
	 * Find part-of-speech or POS tags of all tokens using POS tagger feature of
	 * Apache OpenNLP.
	 *
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	private static String[] detectPOSTags(String[] tokens) throws IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-pos-maxent.bin")) {

			// Initialize POS tagger tool
			POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

			// Tag sentence.
			String[] posTokens = myCategorizer.tag(tokens);
			System.out.println("POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));

			return posTokens;

		}

	}

	/**
	 * Find lemma of tokens using lemmatizer feature of Apache OpenNLP.
	 *
	 * @param tokens
	 * @param posTags
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private static String[] lemmatizeTokens(String[] tokens, String[] posTags)
			throws InvalidFormatException, IOException {
		// Better to read file once at start of program & store model in instance
		// variable. but keeping here for simplicity in understanding.
		try (InputStream modelIn = new FileInputStream("en-lemmatizer.bin")) {

			// Tag sentence.
			LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
			String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);
			System.out.println("Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")));

			return lemmaTokens;
		}
	}
}
