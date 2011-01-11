package utafx.ui.pref;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

public class FileViewer extends JPanel {

    protected static final Component PREVIEW_DISABLED = new JLabel(
	    "Preview disabled");
    protected static final Component PREVIEW_ENABLED = new JLabel(
	    "Preview enabled");
    private JCheckBox preview;
    private JSpinner startLineSlider;
    private JSpinner endLineSlider;

    private JSpinner startColumnSlider;
    private JSpinner endColumnSlider;
    private JComponent dataView;
    private JPanel topPanel;
    private JPanel contentPanel;
    private JPanel selectionPanel;

    public FileViewer() {
	setLayout(new BorderLayout());
	createComponents();
    }

    private void createComponents() {
	startLineSlider = new JSpinner();

	endLineSlider = new JSpinner();
	startColumnSlider = new JSpinner();
	endColumnSlider = new JSpinner();
	preview = new JCheckBox();
	topPanel = new JPanel(new GridLayout(2, 1));
	JPanel startPanel = createStartPanel();
	JPanel endPanel = createEndPanel();

	selectionPanel = new JPanel(new GridLayout(1, 2));
	selectionPanel.add(startPanel);
	selectionPanel.add(endPanel);

	JPanel previewPanel = new JPanel();
	previewPanel.add(preview);
	previewPanel.add(new JLabel("Enable preview"));

	topPanel.add(previewPanel);
	topPanel.add(selectionPanel);

	contentPanel = new JPanel();
	contentPanel.add(new JScrollPane(dataView));

	add(topPanel, BorderLayout.NORTH);
	add(contentPanel, BorderLayout.CENTER);

	preview.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (!(((JCheckBox) (e.getSource())).isSelected())) {
		    System.out.println("Unselected!!!");
		    selectionPanel.setEnabled(false);
		    contentPanel.removeAll();
		    contentPanel.add(PREVIEW_DISABLED);
		} else {
		    System.out.println("Selected!!!");
		    selectionPanel.setEnabled(true);
		    contentPanel.removeAll();
		    contentPanel.add(PREVIEW_ENABLED);
		}
		invalidate();
	    }
	});
    }

    private JPanel createEndPanel() {
	return createPostiotionPanel(endLineSlider, endColumnSlider,
		"End Point");
    }

    private JPanel createStartPanel() {
	return createPostiotionPanel(startLineSlider, startColumnSlider,
		"Start Point");
    }

    private JPanel createPostiotionPanel(JComponent lineSlide,
	    JComponent columnSlide, String borderText) {
	JPanel panel = new JPanel(new GridLayout(2, 2));
	panel.add(new JLabel("Line:"));
	panel.add(lineSlide);
	panel.add(new JLabel("Column:"));
	panel.add(columnSlide);
	panel.setBorder(BorderFactory.createTitledBorder(borderText));
	return panel;
    }

    public static void main(String[] args) {
	JFileChooser jfc = new JFileChooser();
	jfc.setAccessory(new FileViewer());
	jfc.showOpenDialog(null);
    }
}
