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


public class twitter4j extends ApplicationFrame implements ItemListener 
{
	protected static int foreign = 0; //initialisation of the graph variables
	protected static int indian = 0; 
	protected static int irr = 0;
	protected static int other = 0;
	protected static int i_one=0,i_two=0,i_three=0,f_one=0,f_two=0,f_three=0;
	public static final int flag1=0;
	public static  Dialog dt;

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
						.get(SentimentAnnotatedTree.class);
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
	Checkbox stweet,asearch;
	CheckboxGroup radio;
	Image img = Toolkit.getDefaultToolkit().createImage("bg.png");
	private Container middlePanel;
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
	stweet=new Checkbox("Advanced Search Tweet",radio, false);
	asearch=new Checkbox("Status update / Extract tweet",radio, false);
	stweet.addItemListener(this);
	asearch.addItemListener(this);
	stweet.setBounds(850,130,165,15);
	asearch.setBounds(850,170,266,15);
	asearch.setFont(font);
	stweet.setFont(font);
	add(h1);
	add(h2);
	add(asearch);
	add(stweet);
	setVisible(true);
	}

	private CategoryDataset createDataset( )
	{  
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  
		final String population="population";
		final String india = "India";        
		final String foreig = "Foreign";
		final String neg = "Ind-Negative";        
		final String neu = "Ind-Neutral";
		final String pos="Ind-Positive";
		final String f_neg = "For-Negative";        
		final String f_neu = "For-Neutral";
		final String f_pos="For-Positive";
		final String pop="Indian";
		final String popf="Foreign";
		dataset.addValue(  indian,population,india);        
		dataset.addValue(  foreign,population,foreig);        
		dataset.addValue( i_one ,pop,pos);
		dataset.addValue( i_two ,pop,neu);
		dataset.addValue( i_three ,pop,neg);
		dataset.addValue( f_one ,popf ,f_pos);
		dataset.addValue( f_two , popf,f_neu );
		dataset.addValue( f_three ,popf,f_neg);
		return dataset; 
	}

	public void itemStateChanged(ItemEvent e) //awt radio button event handlers
	{ 
		if(stweet.getState() == true)
		{flag=true;
		prepareGUI();
		}
		else if(asearch.getState() == true)
		{  
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
		Label h1=new Label();
		Label h2=new Label();
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
		Label exLabel = new Label();        
		exLabel.setAlignment(Label.CENTER);
		exLabel.setText("\nENTER THE USER HANDLE TO EXTRACT:");
		exLabel.setBounds(520,70,80,25);
		exLabel.setFont(font);
		exLabel.setBackground(Color.YELLOW);
		exLabel.setForeground(Color.BLACK);
		final TextField exText = new TextField(15);
		Button exbutton = new Button("CLICK TO EXTRACT");
		upbutton.setFont(font);
		exbutton.setFont(font);
		Button close=new Button("Close");
		close.setFont(font);
		header.add(h1);
		header.add(h2);
		controlPanel.add(statusLabel);
		controlPanel.add(userText);
		controlPanel.add(upbutton);
		controlPanel.add(close);
		endPanel.add(exLabel);
		endPanel.add(exText);
		endPanel.add(exbutton);
		mainFrame.setVisible(true);

		upbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  

				try {
					if((exText.getText().length()<=3)||(exText.getText().matches("[0-9]+")))//testing if input contains invalid or desired minimum char to carry out the operation
					{
						Frame window=new Frame();
						dt=new Dialog(window,"alert",true);
						dt.setLayout(new FlowLayout());
						Button ok=new Button("OK");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dt.setVisible(false);}});
						dt.add(new Label("Click OK and give valid input"));
						dt.setTitle("Check input");
						dt.setBounds(900,350,900,500);
						dt.setBackground(null);
						dt.add(ok);
						dt.pack();
						dt.setVisible(true);
						System.exit(5);
					}
					else
					{

						String status;
						status = exText.getText();
						Status status1 = twitter.updateStatus(status);
					}				
				}
				catch (TwitterException e1) {
					
					e1.printStackTrace();
				}         

			}});
		exbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				List<Status> statusList = null;
				String user=exText.getText();
				try {
					if((exText.getText().length()<=3)||(exText.getText().matches("[0-9]+")))//testing if input contains invalid or desired minimum char to carryout the operation
					{
						Frame window=new Frame();
						dt=new Dialog(window,"alert",true);
						dt.setLayout(new FlowLayout());
						Button ok=new Button("OK");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dt.setVisible(false);}});
						dt.add(new Label("Click OK and give valid input"));
						dt.setTitle("Check input");
						dt.setBounds(900,350,900,500);
						dt.setBackground(null);
						dt.add(ok);
						dt.pack();
						dt.setVisible(true);
						System.exit(5);
					}
					else
					{

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
		h2.setText("WELCOME TO SEARCH TWEET OPTION");
		h2.setBounds(750,90,230,55);
		h2.setBackground(Color.YELLOW);
		h2.setForeground(Color.BLACK);
		searchLabel = new Label();        
		searchLabel.setAlignment(Label.CENTER);
		searchLabel.setText("\nENTER THE SEARCH WORD:");
		searchLabel.setBounds(700,70,80,25);
		searchLabel.setFont(font);
		searchLabel.setBackground(Color.YELLOW);
		searchLabel.setForeground(Color.BLACK);
		final TextField userText = new TextField(15);
		Button sbutton = new Button("CLICK TO SEARCH");
		sbutton.setFont(font);
		header.add(h1);
		controlPanel.add(searchLabel);
		controlPanel.add(userText);
		controlPanel.add(sbutton);
		mainFrame.setVisible(true);
		Button cbutton = new Button("CLICK HERE FOR GRAPHICS VIEW");
		Button close=new Button("Close");
		close.setFont(font);
		cbutton.setFont(font);
		sbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				try {
					if((userText.getText().length()<=3)||(userText.getText().matches("[0-9]+")))//testing if input contains invalid or desired minimum char to carryout the operation
					{
						Frame window=new Frame();
						dt=new Dialog(window,"alert",true);
						dt.setLayout(new FlowLayout());
						Button ok=new Button("OK");
						ok.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){twitter4j.dt.setVisible(false);}});
						dt.add(new Label("Click OK and give valid input"));
						dt.setTitle("Check input");
						dt.setBounds(900,350,900,500);
						dt.setBackground(null);
						dt.add(ok);
						dt.pack();
						dt.setVisible(true);
						System.exit(5);
					}
					else
					{
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
						controlPanel.add(cbutton);
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

		cbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){  
				chartd("Comparision between Indian and Foreign twitter users"); //Chart name passed to the constructor
				pack( );        
				RefineryUtilities.centerFrameOnScreen(mainFrame);        
				setVisible( true );         
			}
		});  
	}

	public void chartd(String chartTitle )
	{
		JFreeChart barChart = ChartFactory.createBarChart(chartTitle,           
				"Country",            
				"No. of users", 
				createDataset(),          
				PlotOrientation.VERTICAL,true, true, false);
		ChartPanel chartPanel = new ChartPanel( barChart );        
		chartPanel.setPreferredSize(new java.awt.Dimension( 300 , 250 ) );        
		setContentPane( chartPanel ); 
	}   





	public static String searchTweet(Twitter tw, String word) throws IOException //function which carries out the separation of the tweet based on location
	{
		String data = " ";
		String state="INDIA india Andhra Pradesh Arunachal Pradesh Assam Bihar Chhattisgarh Goa Gujarat Haryana Himachal Pradesh Jammu and Kashmir Jharkhand Karnataka Kerala Madhya Pradesh Maharashtra Manipur Meghalaya Mizoram Nagaland Orissa Punjab Rajasthan Sikkim TamilNadu Tripura Uttarakhand Uttar Pradesh West Bengal Tamil Nadu Tripura Andaman and Nicobar Islands Chandigarh Dadra and Nagar Haveli Daman and Diu Delhi Lakshadweep Pondicherry ";
		File fp = new File("/home/siva/Downloads/city.txt");
		Scanner scanner=new Scanner(fp);
		ArrayList<String> list=new ArrayList<String>();
		while(scanner.hasNextLine()){
			list.add(scanner.nextLine()); 
		}
		scanner.close();
		try { int count=1;
		System.out.println("Search in progress...");
		Query query = new Query(word);
		QueryResult result;
		do {
			result = tw.search(query);
			List<Status> tweets = result.getTweets();
			for (Status tweet : tweets) {
				if(tweet.getPlace()!=null)
				{System.out.println(tweet.getUser().getName() + " - "+"Place : "+tweet.getPlace()+"Location:"+tweet.getUser().getLocation());
				if(tweet.getPlace().getCountryCode().equalsIgnoreCase("IN")||(tweet.getPlace().getCountryCode().equalsIgnoreCase("IND")))     
				{indian+=1;

				if(findSentiment(tweet.getText())==1)
				{i_two++;System.out.println("\n--Negative--\n");}
				else if(findSentiment(tweet.getText())==2)
				{i_one++;System.out.println("\n--Neutral--\n");}
				else
				{i_three++;System.out.println("\n--Positive--\n");}

				}
				else                     
				{
					foreign+=1;

					if(findSentiment(tweet.getText())==1)
					{f_two++;System.out.println("\n--Negative--\n");}
					else if(findSentiment(tweet.getText())==2)
					{f_one++;System.out.println("\n--Neutral--\n");}
					else
					{f_three++;System.out.println("\n--Positive--\n");}                       

				}
				}
				else
				{   System.out.println(tweet.getUser().getName() + " - "+"Location:"+tweet.getUser().getLocation()+"\tTime:"+tweet.getUser().getUtcOffset());               	   
				if(list.contains(tweet.getUser().getLocation())&&(tweet.getUser().getLocation().length()>=0))
				{
					indian+=1;

					if(findSentiment(tweet.getText())==1)
					{i_two++;System.out.println("\n--Negative--\n");}
					else if(findSentiment(tweet.getText())==2)
					{i_one++;System.out.println("\n--Neutral--\n");}
					else
					{i_three++;System.out.println("\n--Positive--\n");}


				}
				else{
					if(((tweet.getUser().getLocation().contains("India"))||(tweet.getUser().getLocation().contains(state))))
					{
						indian+=1;

						if(findSentiment(tweet.getText())==1)
						{i_two++;System.out.println("\n--Negative--\n");}
						else if(findSentiment(tweet.getText())==2)
						{i_one++;System.out.println("\n--Neutral--\n");}
						else
						{i_three++;System.out.println("\n--Positive--\n");}
					}
					else
					{  	if((tweet.getUser().getUtcOffset()!=19800)&&(tweet.getUser().getUtcOffset()!=-1))
					{
						foreign+=1;

						if(findSentiment(tweet.getText())==1)
						{f_two++;System.out.println("\n--Negative--\n");}
						else if(findSentiment(tweet.getText())==2)
						{f_one++;System.out.println("\n--Neutral--\n");}
						else
						{f_three++;System.out.println("\n--Positive--\n");}   

					}
					else{ 
						if((tweet.getUser().getUtcOffset()==-1))
						{ if((tweet.getUser().getLocation().length()==0)||(tweet.getUser().getLocation().length()==1))
							irr+=1;
						else
						{

							foreign+=1;

							if(findSentiment(tweet.getText())==1)
							{f_two++;System.out.println("\n--Negative--\n");}
							else if(findSentiment(tweet.getText())==2)
							{f_one++;System.out.println("\n--Neutral--\n");}
							else
							{f_three++;System.out.println("\n--Positive--\n");}   

						}
						}
						else	
						{
							if((tweet.getUser().getLocation().length()==0)||(tweet.getUser().getLocation().length()==1))
							{
								foreign+=1;

								if(findSentiment(tweet.getText())==1)
								{f_two++;System.out.println("\n--Negative--\n");}
								else if(findSentiment(tweet.getText())==2)
								{f_one++;System.out.println("\n--Neutral--\n");}
								else
								{f_three++;System.out.println("\n--Positive--\n");}   
							}
							else{
								if(!(tweet.getUser().getLocation().contains("Lanka")))
								{
									indian+=1;

									if(findSentiment(tweet.getText())==1)
									{i_two++;System.out.println("\n--Negative--\n");}
									else if(findSentiment(tweet.getText())==2)
									{i_one++;System.out.println("\n--Neutral--\n");}
									else
									{i_three++;System.out.println("\n--Positive--\n");}
								}
								else
								{
									foreign+=1;

									if(findSentiment(tweet.getText())==1)
									{f_two++;System.out.println("\n--Negative--\n");}
									else if(
											findSentiment(tweet.getText())==2)
									{f_one++;System.out.println("\n--Neutral--\n");}
									else
									{f_three++;System.out.println("\n--Positive--\n");}   
								}
							}
						}
					}
					}
				}
				}

				{FileWriter file = new FileWriter("/home/siva/Documents/file.txt",true);
				BufferedWriter buf = new BufferedWriter(file);
				buf.flush();
				data+="\n\n"+count+". \nNAME:"+tweet.getUser().getName()+"\nLocation:"+tweet.getUser().getLocation()+" - "+"\nTWEET:"+tweet.getText();
				buf.append("\nName:"+tweet.getUser().getName()+ " - "+"Location:"+tweet.getUser().getLocation()+"\nTweet:"+tweet.getText()+"\n");
				buf.close();
				count+=1;}
			}//end for
		}while ((query = result.nextQuery()) != null);//end do while

		}///end try
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			data="\nFailed to search tweets because of rate limiting...";
		}
		System.out.println("\nIndia:"+indian+"\nForeign:"+foreign+"\nOther:"+other+"\nIrr:"+irr);
		System.out.println("pos:"+i_three+"Neu:"+i_two+"neg:"+i_one);
		System.out.println("fpos:"+f_three+"Neu:"+f_two+"neg:"+f_one);

		return data;
	}	
}
