package twitter4j;
import java.awt.*;
import java.lang.Object.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;

import org.omg.CORBA.Environment;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Status;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


@SuppressWarnings("serial")
public class twitter4j extends ApplicationFrame implements ItemListener 
{
	protected static int foreign = 0; //Initialization of the graph variables
	protected static int indian = 0; 
	protected static int irr = 0;
	protected static int other = 0;
	protected static int indiaOne=0,indiaTwo=0,indiaThree=0,foreignOne=0,foreignTwo=0,foreignThree=0;
	public static final int flag1=0;
	public static  Dialog dialog;

	public static int findSentiment(String tweet) {   //function that finds the polarity of the extracted tweet
		StanfordCoreNLP pipeline;
		pipeline = new StanfordCoreNLP("MyPropFile.properties");
		int mainSentiment = 0;
		if (tweet != null && tweet.length() > 0) {
			int longest = 0;
			Annotation annotation = pipeline.process(tweet);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree =sentence
						.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		return mainSentiment;
	}



	public twitter4j(String title)
	{	super(title);

	}
	public static boolean flag=false;
	public Frame mainFrame;
	public Label h1,h2,h3;
	public Panel header;
	public Panel controlPanel;
	public Label searchLabel;
	public Label statusLabel;
	public Panel endPanel;
	public Label lab;
	Checkbox searchTweet,activeSearch;
	CheckboxGroup radio;
	Image img = Toolkit.getDefaultToolkit().createImage("bg.png");
	public void paint(Graphics g)
	{
		g.drawImage(img, 0, 0, null);
	}
	public void initi()  //Chart init parameters
	{  setLayout(null);
	setBackground(Color.orange);
	setForeground(Color.blue);
	Label h1=new Label();
	Font font = new Font("TimesNewRoman", Font.BOLD,15);
	h1.setText("WELCOME TO TWITTER DATA MINING");
	h1.setFont(font);
	h1.setForeground(Color.black);
	h1.setBackground(Color.yellow);
	h1.setBounds(740,50,320,20);
	Label h2=new Label();
	h2.setText("SELECT ANY ONE OF THE OPERATION THAT YOU WISH TO PERFORM:");
	h2.setForeground(Color.red);
	h2.setBackground(Color.yellow);
	h2.setFont(font);
	h2.setBounds(600,90,580,20);
	this.setSize(new Dimension(2500,2500));
	radio=new CheckboxGroup(); 
	searchTweet=new Checkbox("Advanced Search Tweet",radio, false);
	activeSearch=new Checkbox("Status update / Extract tweet",radio, false);
	searchTweet.addItemListener(this);
	activeSearch.addItemListener(this);
	searchTweet.setBounds(850,130,165,15);
	activeSearch.setBounds(850,170,266,15);
	activeSearch.setFont(font);
	searchTweet.setFont(font);
	add(h1);
	add(h2);
	add(activeSearch);
	add(searchTweet);
	setVisible(true);
	}

	private CategoryDataset createDataset( ){  
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
		final String population    = "population";
		final String india         = "India";        
		final String restOfIndia   = "Foreign";
		final String iNegative 	   = "Ind-Negative";        
		final String iNeutral      = "Ind-Neutral";
		final String iPositive     = "Ind-Positive";
		final String fNegative     = "For-Negative";        
		final String fNeutral      = "For-Neutral";
		final String fPositive     = "For-Positive";
		final String iPopulation   = "Indian";
		final String fPopulation   = "Foreign";
		dataset.addValue( indian,       population,india);        
		dataset.addValue( foreign,      population,restOfIndia);        
		dataset.addValue( indiaOne,     iPopulation,iNegative);
		dataset.addValue( indiaTwo,     iPopulation,iNeutral);
		dataset.addValue( indiaThree,   iPopulation,iPositive);
		dataset.addValue( foreignOne,   fPopulation,fNegative);
		dataset.addValue( foreignTwo,   fPopulation,fNeutral );
		dataset.addValue( foreignThree, fPopulation,fPositive);
		return dataset; 
	}

	public void itemStateChanged(ItemEvent e){ //awt radio button event handlers 
		if(searchTweet.getState() == true){
			flag=true;
			prepareGUI();
		}
		else if(activeSearch.getState() == true){  
			GUI();
		}
	}

	public static void main(String[] args) throws IOException{  
		twitter4j chart = new twitter4j("Twitter data mining");
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)   //OAuth parameters declaration
		.setOAuthConsumerKey("6PmLszC988jCNUrpyv75yEqlN")
		.setOAuthConsumerSecret("kK1Jtfj3BnvoJ8SMlcVkYWuBLo6qa7HZocCujlwcdjJ3ahUcac")
		.setOAuthAccessToken("275418825-MpiHg0tAnrm26UtbFxsTHgdohB9ji3d2j2yGZinF")
		.setOAuthAccessTokenSecret("4OsnHrUgBM9ovoeqpE4ovCopoiEg6nNepDrLry8pdYatJ");
		cb.build();
		chart.initi();
	}

	public void prepareGUI(){
		mainFrame = new Frame("Twitter Data Mining");
		mainFrame.setSize(2500,2500);
		mainFrame.setBackground(Color.orange);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    
		h1 = new Label();
		h1.setAlignment(Label.CENTER);
		h2 = new Label();
		h2.setAlignment(Label.CENTER);
		header = new Panel();
		header.setLayout(null);
		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());
		endPanel = new Panel();
		endPanel.setLayout(new FlowLayout());
		mainFrame.add(header);
		mainFrame.add(controlPanel);
		mainFrame.add(endPanel);
		mainFrame.setVisible(true);
		showTextFieldDemo();
	}

	public void GUI(){
		mainFrame = new Frame("Twitter Data Mining");
		mainFrame.setSize(2500,2500);
		mainFrame.setBackground(Color.pink);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    
		h1 = new Label();
		h1.setAlignment(Label.CENTER);
		h2 = new Label();
		h2.setAlignment(Label.CENTER);
		header = new Panel();
		header.setLayout(null);
		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());
		endPanel = new Panel();
		endPanel.setLayout(new FlowLayout());
		mainFrame.add(header);
		mainFrame.add(controlPanel);
		mainFrame.add(endPanel);
		mainFrame.setVisible(true);
		showTextField();
	}

	public void showTextField(){
		Twitter twitter = new TwitterFactory().getInstance();
		Font font = new Font("Courier", Font.BOLD,16);
		Label h1  = new Label();
		Label h2  = new Label();
		h1.setText("WELCOME TO TWITTER DATA MINING");
		h1.setBounds(820,20,235,25);
		h1.setBackground(Color.YELLOW);
		h1.setForeground(Color.BLACK);
		h1.setFont(font);
		h2.setText("WELCOME TO UPDATE/EXTRACT TWEET OPTION");
		h2.setBounds(730,70,385,35);
		h2.setBackground(Color.YELLOW);
		h2.setForeground(Color.BLACK);
		h2.setFont(font);
		this.setSize(new Dimension(2500,2500));
		setVisible(true);
		statusLabel = new Label();        
		statusLabel.setAlignment(Label.CENTER);
		statusLabel.setText("\nENTER THE STATUS TO BE UPDATED:");
		statusLabel.setBounds(700,70,80,25);
		statusLabel.setFont(font);
		statusLabel.setBackground(Color.YELLOW);
		statusLabel.setForeground(Color.BLACK);
		final TextField userText = new TextField(15);
		Button upbutton = new Button("CLICK TO UPDATE");
		upbutton.setFont(font);
		Label extractLabel = new Label();        
		extractLabel.setAlignment(Label.CENTER);
		extractLabel.setText("\nENTER THE USER HANDLE TO EXTRACT:");
		extractLabel.setBounds(520,70,80,25);
		extractLabel.setFont(font);
		extractLabel.setBackground(Color.YELLOW);
		extractLabel.setForeground(Color.BLACK);
		final TextField extractText = new TextField(15);
		Button extractButton = new Button("CLICK TO EXTRACT");
		upbutton.setFont(font);
		extractButton.setFont(font);
		Button close = new Button("Close");
		close.setFont(font);
		header.add(h1);
		header.add(h2);
		controlPanel.add(statusLabel);
		controlPanel.add(userText);
		controlPanel.add(upbutton);
		controlPanel.add(close);
		endPanel.add(extractLabel);
		endPanel.add(extractText);
		endPanel.add(extractButton);
		mainFrame.setVisible(true);

		upbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				try {
					if((extractText.getText().length()<=3)||(extractText.getText().matches("[0-9]+"))){//testing if input contains invalid or desired minimum char to carry out the operation
						Frame window=new Frame();
						dialog=new Dialog(window,"alert",true);
						dialog.setLayout(new FlowLayout());
						Button ok=new Button("Close");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dialog.setVisible(false);}});
						dialog.add(new Label("Click Close and give valid input"));
						dialog.setTitle("Check input");
						dialog.setBounds(900,350,900,500);
						dialog.setBackground(null);
						dialog.add(ok);
						dialog.pack();
						dialog.setVisible(true);
						System.exit(5);
					}
					else{
						String status;
						status = extractText.getText();
						Status status1 = twitter.updateStatus(status);
					}				
				}
				catch (TwitterException e1) {
					e1.printStackTrace();
				}         
			}});
		extractButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				List<Status> statusList = null;
				String user=extractText.getText();
				try {
					if((extractText.getText().length()<=3)||(extractText.getText().matches("[0-9]+"))){//testing if input contains invalid or desired minimum char to carryout the operation
						Frame window=new Frame();
						dialog=new Dialog(window,"alert",true);
						dialog.setLayout(new FlowLayout());
						Button ok=new Button("Close");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dialog.setVisible(false);}});
						dialog.add(new Label("Click Close and give valid input"));
						dialog.setTitle("Check input");
						dialog.setBounds(900,350,900,500);
						dialog.setBackground(null);
						dialog.add(ok);
						dialog.pack();
						dialog.setVisible(true);
						System.exit(5);
					}
					else{
						statusList = twitter.getUserTimeline(user);
						for (Status status : statusList) {
							System.out.println(status.toString());
						}
					}
				}
				catch (TwitterException e1) {
					e1.printStackTrace();
				}
			}
		});
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){ 
				System.exit(0);
			}
		}); 
	}



	public void showTextFieldDemo(){
		Twitter twitter = new TwitterFactory().getInstance();
		Font font = new Font("Courier", Font.BOLD,12);
		h1.setText("WELCOME TO TWITTER DATA MINING");
		h1.setBounds(840,50,225,35);
		h1.setBackground(Color.YELLOW);
		h1.setForeground(Color.BLACK);
		h1.setFont(font);
		h2.setText("YOU ARE AT THE SEARCH TWEET OPTION");
		h2.setBounds(750,90,230,55);
		h2.setBackground(Color.YELLOW);
		h2.setForeground(Color.BLACK);
		searchLabel = new Label();        
		searchLabel.setAlignment(Label.CENTER);
		searchLabel.setText("\nENTER THE TOPIC:");
		searchLabel.setBounds(700,70,80,25);
		searchLabel.setFont(font);
		searchLabel.setBackground(Color.YELLOW);
		searchLabel.setForeground(Color.BLACK);
		final TextField userText = new TextField(15);
		Button searchButton = new Button("CLICK TO SEARCH");
		searchButton.setFont(font);
		header.add(h1);
		controlPanel.add(searchLabel);
		controlPanel.add(userText);
		controlPanel.add(searchButton);
		mainFrame.setVisible(true);
		Button visualisingButton = new Button("CLICK HERE FOR VISUALISING");
		Button close=new Button("Close");
		close.setFont(font);
		visualisingButton.setFont(font);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				try {
					if((userText.getText().length()<=3)||(userText.getText().matches("[0-9]+"))){//testing if input contains invalid or desired minimum char to carryout the operation
						Frame window=new Frame();
						dialog=new Dialog(window,"alert",true);
						dialog.setLayout(new FlowLayout());
						Button ok=new Button("OK");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dialog.setVisible(false);}});
						dialog.add(new Label("Click OK and give valid input"));
						dialog.setTitle("Check input");
						dialog.setBounds(900,350,900,500);
						dialog.setBackground(null);
						dialog.add(ok);
						dialog.pack();
						dialog.setVisible(true);
						System.exit(5);
					}
					else{
						String data=searchTweet(twitter,userText.getText());
						System.out.println("After returned:"+data);
						statusLabel = new Label();        
						statusLabel.setLocation(400,130);
						statusLabel.setText("RESULT");
						statusLabel.setBackground(Color.YELLOW);
						statusLabel.setForeground(Color.BLACK);
						final TextArea result=new TextArea(data,80,150);
						endPanel.add(statusLabel);
						endPanel.add(result);
						controlPanel.add(visualisingButton);
						controlPanel.add(close);
						mainFrame.setVisible(true);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}         
			}
		});
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){ 
				System.exit(0);
			}
		});  

		visualisingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				chartd("Comparison between Indian and Foreign twitter users for the topic"+userText.getText()); //Chart name passed to the constructor
				pack( );        
				RefineryUtilities.centerFrameOnScreen(mainFrame);        
				setVisible( true );         
			}
		});  
	}

	public void chartd(String chartTitle ){
		JFreeChart barChart = ChartFactory.createBarChart(chartTitle,           
				"Country",            
				"No. of users", 
				createDataset(),          
				PlotOrientation.VERTICAL,true, true, false);
		ChartPanel chartPanel = new ChartPanel( barChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 300 , 250 ) );        
		setContentPane( chartPanel ); 
	}   


    public static int  getSentimentAndReturnScore(String tweet){
    	int score = findSentiment(tweet);
		System.out.println("Score:"+score);
		if(score  <= 1){
			System.out.println("\n--Negative--\n");
		}
		else if(score == 2){
			System.out.println("\n--Neutral--\n");
		}
		else{
			System.out.println("\n--Positive--\n");
		}
    	return score;
    }


	public static String searchTweet(Twitter tw, String word) throws IOException{ //function which carries out the separation of the tweet based on location
		StringBuilder data = new StringBuilder();
		String state="INDIA india Andhra Pradesh Arunachal Pradesh Assam Bihar Chhattisgarh Goa Gujarat Haryana Himachal Pradesh Jammu and Kashmir Jharkhand Karnataka Kerala Madhya Pradesh Maharashtra Manipur Meghalaya Mizoram Nagaland Orissa Punjab Rajasthan Sikkim TamilNadu Tripura Uttarakhand Uttar Pradesh West Bengal Tamil Nadu Tripura Andaman and Nicobar Islands Chandigarh Dadra and Nagar Haveli Daman and Diu Delhi Lakshadweep Pondicherry ";
		File fp = new File("/home/siva/Downloads/city.txt");
		Scanner scanner=new Scanner(fp);
		ArrayList<String> list=new ArrayList<String>();
		while(scanner.hasNextLine()){
			list.add(scanner.nextLine()); 
		}
		scanner.close();
		try {
			int count=1;
			System.out.println("Search in progress...");
			Query query = new Query(word);
			QueryResult result;
			do {
				result = tw.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					int score = getSentimentAndReturnScore(tweet.getText());
					if(tweet.getPlace()!=null){
						System.out.println(tweet.getUser().getName() + " - "+"Place : "+tweet.getPlace()+"Location:"+tweet.getUser().getLocation());
						if(tweet.getPlace().getCountryCode().equalsIgnoreCase("IN")||(tweet.getPlace().getCountryCode().equalsIgnoreCase("IND"))){
							indian+=1;
							if(score  <= 1)
								indiaOne++;
							else if(score == 2)
								indiaTwo++;
							else
								indiaThree++;
						}
						else{
							foreign+=1;
							if(score  <= 1)
								foreignOne++;
							else if(score == 2)
								foreignTwo++;
							else
								foreignThree++;                       
						}
					}
					else{   
						System.out.println(tweet.getUser().getName() + " - "+"Location:"+tweet.getUser().getLocation()+"\tTime:"+tweet.getUser().getUtcOffset());               	   
						if(list.contains(tweet.getUser().getLocation())&&(tweet.getUser().getLocation().length()>=0)){
							indian+=1;
							if(score  <= 1)
								indiaOne++;
							else if(score == 2)
								indiaTwo++;
							else
								indiaThree++;
						}
						else{
							if(((tweet.getUser().getLocation().contains("India"))||(tweet.getUser().getLocation().contains(state)))){
								indian+=1;
								if(score  <= 1)
									indiaOne++;
								else if(score == 2)
									indiaTwo++;
								else
									indiaThree++;
							}
							else{  	
								if((tweet.getUser().getUtcOffset()!=19800)&&(tweet.getUser().getUtcOffset()!=-1)){
									foreign+=1;
									if(score  <= 1)
										foreignOne++;
									else if(score == 2)
										foreignTwo++;
									else
										foreignThree++;   
								}
								else{ 
									if((tweet.getUser().getUtcOffset()==-1)){ 
										if((tweet.getUser().getLocation().length()==0)||(tweet.getUser().getLocation().length()==1))
											irr+=1;
										else{
											foreign+=1;
											if(score  <= 1)
												foreignOne++;
											else if(score == 2)
												foreignTwo++;
											else
												foreignThree++;   
										}
									}
									else{
										if((tweet.getUser().getLocation().length()==0)||(tweet.getUser().getLocation().length()==1)){
											foreign+=1;
											if(score  <= 1)
												foreignOne++;
											else if(score == 2)
												foreignTwo++;
											else
												foreignThree++;    
										}	
										else{
											if(!(tweet.getUser().getLocation().contains("Lanka"))){
												indian+=1;
												if(score  <= 1)
													indiaOne++;
												else if(score == 2)
													indiaTwo++;
												else
													indiaThree++; 
											}
											else{
												foreign+=1;
												if(score  <= 1)
													foreignOne++;
												else if(score == 2)
													foreignTwo++;
												else
													foreignThree++;    
											}
										}
									}
								}
							}
						}
					}
					{
					FileWriter file = new FileWriter("/home/siva/Documents/file.txt",true);
					BufferedWriter bufferedWriter = new BufferedWriter(file);
					bufferedWriter.flush();
					data.append("\n\n"+count+". \nNAME:"+tweet.getUser().getName()+"\nLocation:"+tweet.getUser().getLocation()+" - "+"\nTWEET:"+tweet.getText()+"\nScore:"+findSentiment(tweet.getText()));
					bufferedWriter.append("\nName:"+tweet.getUser().getName()+ " - "+"Location:"+tweet.getUser().getLocation()+"\nTweet:"+tweet.getText()+"\nScore:"+findSentiment(tweet.getText())+"\n");
					bufferedWriter.close();
					count+=1;
					}
				}//end for
			}while ((query = result.nextQuery()) != null);//end do while
		}///end try
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			data.append("\nFailed to search tweets because of rate limiting...");
		}
		System.out.println("\nIndia:"+indian+"\nForeign:"+foreign+"\nOther:"+other+"\nIrr:"+irr);
		System.out.println("India +ve:"+indiaThree+"India-Neutral:"+indiaTwo+"India-ve:"+indiaOne);
		System.out.println("Foreign+ve:"+foreignThree+"Foreign-Neutral:"+foreignTwo+"Foreign-ve:"+foreignOne);
		//int score = findSentiment(word);
		//System.out.println("Score:"+score);
		return data.toString();
	}	
}
