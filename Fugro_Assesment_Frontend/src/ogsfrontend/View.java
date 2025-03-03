package ogsfrontend;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.furgo.frontend.rcp.model.Location;
import com.furgo.frontend.rcp.model.Sample;
import com.furgo.frontend.rcp.service.ApiService;

public class View extends ViewPart {
	private Table table;
	private ApiService apiService = new ApiService();
	private Sample sample;

	private DateTime dateCollectedPicker;
	private Text unitWeightText;
	private Text waterContentText;
	private Text shearStrengthText;
	private Location selectedLocation;
	private int rowId = 1;
	
	

	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());

		// Create Table
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true); // Show headers
		table.setLinesVisible(true); // Show grid lines

		// Define Columns
		String[] columnNames = { "Id", "Location Name", "Date Collected", "Unit Weight", "Water Content",
				"Shear Strength", "Delete" };
		int[] columnWidths = { 100, 150, 150, 150, 150, 150,  100 };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnNames[i]);
			column.setWidth(columnWidths[i]);
		}

		loadSamples();
		Button addButton = new Button(parent, SWT.PUSH);
		addButton.setText("Add Sample");

		addButton.addListener(SWT.Selection, e -> openAddDialog(parent));
	}

	private void openAddDialog(Composite parent) {

		Shell shell = new Shell(parent.getShell());
		shell.setText("Add Sample");

		shell.setLayout(new GridLayout(5, false));

		// Location field
		Label locationLabel = new Label(shell, SWT.NONE);
		locationLabel.setText("Location:");
		ComboViewer locationCombo = new ComboViewer(shell, SWT.DROP_DOWN);
		locationCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// fetch data from Locations Entity
		try {
			List<Location> alllocations = apiService.fetchLocations();
			locationCombo.setContentProvider(new ArrayContentProvider());
			locationCombo.setInput(alllocations);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// Date Collected field
		Label dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setText("Date Collected:");
		dateCollectedPicker = new DateTime(shell, SWT.DATE | SWT.DROP_DOWN);
		dateCollectedPicker.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Unit Weight field
		Label unitWeightLabel = new Label(shell, SWT.NONE);
		unitWeightLabel.setText("Unit Weight:");
		unitWeightText = new Text(shell, SWT.BORDER);
		unitWeightText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Water Content field
		Label waterContentLabel = new Label(shell, SWT.NONE);
		waterContentLabel.setText("Water Content:");
		waterContentText = new Text(shell, SWT.BORDER);
		waterContentText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Shear Strength field
		Label shearStrengthLabel = new Label(shell, SWT.NONE);
		shearStrengthLabel.setText("Shear Strength:");
		shearStrengthText = new Text(shell, SWT.BORDER);
		shearStrengthText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button saveButton = new Button(shell, SWT.PUSH);
		saveButton.setText("Save");

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");

		locationCombo.getCombo().addListener(SWT.Selection, e -> {
			ISelection selection = locationCombo.getSelection();
			if (selection instanceof StructuredSelection) {
				selectedLocation = (Location) ((StructuredSelection) selection).getFirstElement();
			}
		});
		saveButton.addListener(SWT.Selection, e -> addSample(sample, shell));
		cancelButton.addListener(SWT.Selection, e -> shell.close());
		shell.pack();
		shell.open();
	}

	private Object addSample(Sample sample, Shell shell) {
		try {
			LocalDate dateCollected = LocalDate.of(dateCollectedPicker.getYear(), dateCollectedPicker.getMonth() + 1,
					dateCollectedPicker.getDay());
			Double unitWeight = Double.parseDouble(unitWeightText.getText());
			Double waterContent = Double.parseDouble(waterContentText.getText());
			Double shearStrength = Double.parseDouble(shearStrengthText.getText());

			sample = new Sample(new Location(selectedLocation.getId(), selectedLocation.getName()), dateCollected,
					unitWeight, waterContent, shearStrength);

			apiService.addSample(sample);
			shell.close();
			loadSamples();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadSamples() {
		table.removeAll();

		try {
			List<Sample> samples = apiService.fetchSamples();
			for (Sample sample : samples) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] { String.valueOf(sample.getId()), sample.getLocation().getName(),
						sample.getDateCollected().toString(), sample.getUnitWeight().toString(),
						sample.getWaterContent().toString(), sample.getShearStrength().toString() });
			
				deleteHyperLink(item, sample.getId(), 6);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteHyperLink(TableItem item, int id, int columnIndex) {
		TableEditor editor = new TableEditor(table);
		Link link = new Link(table, SWT.NONE);
		link.setText("<a>Delete</a>"); // Displayed as hyperlink
		link.addListener(SWT.Selection, e -> {
			
			try {
				apiService.deleteSample(id);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			loadSamples();
		});
		// Assign link to the correct column
		editor.grabHorizontal = true;
		editor.setEditor(link, item, columnIndex);
	}

	public void editHyperlink(TableItem item, int columnIndex) {
		TableEditor editor = new TableEditor(table);
		Link link = new Link(table, SWT.NONE);
		if (columnIndex == 6) {
			link.setText("<a>Edit</a>");// Displayed as hyperlink
			link.addSelectionListener(new SelectionAdapter() {
				public void editSample(SelectionEvent e) {
					// Do nothing
				}
			});
			link.addListener(SWT.Selection, e -> {
				editSample();
			});
		}
		editor.grabHorizontal = true;
		editor.setEditor(link, item, columnIndex);
	}
	
	public void editSample() {
		//Display display= new Display();
		Shell shell = this.getShell();
		shell.setText("Edit Sample");

		shell.setLayout(new GridLayout(5, false));

		// Location field
		Label locationLabel = new Label(shell, SWT.NONE);
		locationLabel.setText("Location:");
		ComboViewer locationCombo = new ComboViewer(shell, SWT.DROP_DOWN);
		locationCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// fetch data from Locations Entity
		try {
			List<Location> alllocations = apiService.fetchLocations();
			locationCombo.setContentProvider(new ArrayContentProvider());
			locationCombo.setInput(alllocations);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// Date Collected field
		Label dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setText("Date Collected:");
		dateCollectedPicker = new DateTime(shell, SWT.DATE | SWT.DROP_DOWN);
		dateCollectedPicker.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Unit Weight field
		Label unitWeightLabel = new Label(shell, SWT.NONE);
		unitWeightLabel.setText("Unit Weight:");
		unitWeightText = new Text(shell, SWT.BORDER);
		unitWeightText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Water Content field
		Label waterContentLabel = new Label(shell, SWT.NONE);
		waterContentLabel.setText("Water Content:");
		waterContentText = new Text(shell, SWT.BORDER);
		waterContentText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Shear Strength field
		Label shearStrengthLabel = new Label(shell, SWT.NONE);
		shearStrengthLabel.setText("Shear Strength:");
		shearStrengthText = new Text(shell, SWT.BORDER);
		shearStrengthText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button saveButton = new Button(shell, SWT.PUSH);
		saveButton.setText("Save");

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");

		locationCombo.getCombo().addListener(SWT.Selection, e -> {
			ISelection selection = locationCombo.getSelection();
			if (selection instanceof StructuredSelection) {
				selectedLocation = (Location) ((StructuredSelection) selection).getFirstElement();
			}
		});
		saveButton.addListener(SWT.Selection, e -> addSample(sample, shell));
		cancelButton.addListener(SWT.Selection, e -> shell.close());
		shell.pack();
		shell.open();
	}
	
	public static Shell getShell() {
	    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    return (window != null) ? window.getShell() : null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}