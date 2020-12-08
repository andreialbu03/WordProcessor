import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Document extends JFrame implements MenuListener, ActionListener, KeyListener, MouseListener {

	//use the deriveFONT
	// USE the JTextPane
	
	// FIND HOW TO GET THE SELECTED TEXT FROM THE STATIC VAIRABLE TO REPLACE THE SELECTED TEXT FROM THE TEXT AREA
	
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenu file, edit, view;
	private JMenuItem save, exit, open;
	private JMenuItem cut, copy, paste, selectAll;
	private JMenuItem bold, italic, underline;
	
	private static int fontSize = 12;
	private static String selectedText;

	public Document() {
		super("Text Editor");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		BorderLayout layout = new BorderLayout();

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(textArea.getFont().deriveFont(12f));
		textArea.addMouseListener(this);
		scrollPane = new JScrollPane(textArea);
				
		menuBar = new JMenuBar();

		// First heading on menu bar
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);

		// Second heading on menu bar
		edit = new JMenu("Edit");
		menuBar.add(edit);

		// Third heading on menu bar
		view = new JMenu("View");
		menuBar.add(view);

		// Forth heading on menu bar
		// Display all fonts
		String[] fontStrings = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		//String[] fontStrings = { "Arial", "Courier", "Helvetica", "Times New Roman"};
		JComboBox fontList = new JComboBox(fontStrings);
		fontList.setMaximumSize(fontList.getPreferredSize());
		
		fontList.addActionListener(new ActionListener() { //add actionlistner to listen for change
			public void actionPerformed(ActionEvent e) {
				
				String name = (String) fontList.getSelectedItem(); //get the selected item
				Font currentFont = new Font(name, Font.PLAIN, fontSize);
				textArea.setFont(currentFont);
			}
		});
		menuBar.add(fontList);
		
		// Fifth heading on menu bar
		// Display fonts sizes
		String[] numFonts = new String[130];
		for (int i=0; i < numFonts.length; i++) {
			numFonts[i] = Integer.toString(i);
		}
		
		JComboBox fontSizeList = new JComboBox(numFonts);
		fontSizeList.setSelectedIndex(12);
		fontSizeList.setMaximumSize(fontSizeList.getPreferredSize());
		
		fontSizeList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//use the deriveFONT
				fontSize = Integer.parseInt((String) fontSizeList.getSelectedItem());
				Font temp = new Font (textArea.getFont().toString(), Font.PLAIN, fontSize);
				textArea.setFont(temp);
			}
		});
		menuBar.add(fontSizeList);

		// Add sub-menus to file, the first sub-heading
		open = new JMenuItem("Open");
		open.addActionListener(this);
		file.add(open);

		save = new JMenuItem("Save");
		save.addActionListener(this);
		file.add(save);

		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		file.add(exit);

		// Add sub-menus to edit, the second sub-heading
		cut = new JMenuItem("Cut");
		cut.addActionListener(this);
		edit.add(cut);

		copy = new JMenuItem("Copy");
		copy.addActionListener(this);
		edit.add(copy);

		paste = new JMenuItem("Paste");
		paste.addActionListener(this);
		edit.add(paste);

		selectAll = new JMenuItem("Select All");
		selectAll.addActionListener(this);
		edit.add(selectAll);

		setLayout(layout);
		this.setJMenuBar(menuBar);
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cut)) {
			textArea.setText("ToDo");
		}
		if (e.getSource().equals(copy)) {
			StringSelection stringSelection = new StringSelection(textArea.getText());
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(stringSelection, null);
		}
		if (e.getSource().equals(paste)) {
			textArea.paste();
		}
		if (e.getSource().equals(selectAll)) {
			textArea.selectAll();
		}
		if (e.getSource().equals(save)) {

			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Save as");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int userSelection = fc.showSaveDialog(this);
			if (userSelection != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File fileName = new File(fc.getSelectedFile() + ".txt");
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(fileName));
				textArea.write(bw);
				System.exit(0);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException ex) {
						// one of the few times that I think that it's OK
						// to leave this blank
					}
				}
			}

		}
		if (e.getSource().equals(open)) {
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int userSelection = fc.showOpenDialog(this);
			if (userSelection != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File filename = new File(fc.getSelectedFile() + "");

			try {
				FileReader reader = new FileReader(filename);
				BufferedReader br = new BufferedReader(reader);
				textArea.read(br, null);
				br.close();
				edit.requestFocus();
			} catch (Exception e2) {
				System.out.println(e2);
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuSelected(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new Document();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (textArea.getSelectedText() != null) {
			selectedText = textArea.getSelectedText();
			System.out.println(selectedText);
		}
	}

}
