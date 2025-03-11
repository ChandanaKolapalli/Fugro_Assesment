package ogsfrontend;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
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
	List<Location> alllocations = null;

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		// === MAIN SASHFORM: SPLITS LEFT AND RIGHT ===
		SashForm mainSash = new SashForm(parent, SWT.HORIZONTAL);
		mainSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite leftComposite = new Composite(mainSash, SWT.BORDER);
		leftComposite.setLayout(new FillLayout());

		table = new Table(leftComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true); // Show headers
		table.setLinesVisible(true); // Show grid lines
		// Define Columns
		String[] columnNames = { "Id", "Location Name", "Date Collected", "Unit Weight(kN/m3)", "Water Content(%)",
				"Shear Strength(kPa)", "Edit", "Delete" };
		int[] columnWidths = { 0, 150, 150, 150, 150, 150, 100, 100 };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnNames[i]);
			column.setWidth(columnWidths[i]);
		}
		loadSamples(parent);

		Composite rightComposite = new Composite(mainSash, SWT.NONE);
		rightComposite.setLayout(new GridLayout(1, false));

		Composite buttonComposite = new Composite(rightComposite, SWT.NONE);
		RowLayout buttonLayout = new RowLayout(SWT.HORIZONTAL);
		buttonLayout.justify = true;
		buttonComposite.setLayout(buttonLayout);
		buttonComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));

		Button addButton = new Button(buttonComposite, SWT.PUSH);
		addButton.setText("Add Sample");
		addButton.addListener(SWT.Selection, e -> openAddDialog(parent));

		Button avgWaterContentButton = new Button(buttonComposite, SWT.PUSH);
		avgWaterContentButton.setText("Get Avg Water Content");
		avgWaterContentButton.addListener(SWT.Selection, e -> {
			fetchAndDisplayAvgWaterContent(parent);
		});

		Button thresholdValues = new Button(buttonComposite, SWT.PUSH);
		thresholdValues.setText("Get Threshold Samples");
		thresholdValues.addListener(SWT.Selection, e -> loadThresholdSamples(parent));	
				
		mainSash.setWeights(new int[] { 1, 1 });
	}


	private void fetchAndDisplayAvgWaterContent(Composite parent) {
		try {

			double avgWaterContent = apiService.getAverageWaterContent();

			Shell shell = parent.getShell();
			Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			dialog.setText("Average Water Content");
			dialog.setLayout(new GridLayout(1, false));

			Label resultLabel = new Label(dialog, SWT.NONE);
			resultLabel.setText("Average Water Content: " + avgWaterContent);

			Button okButton = new Button(dialog, SWT.PUSH);
			okButton.setText("OK");
			okButton.addListener(SWT.Selection, e -> dialog.close());

			dialog.pack();
			dialog.open();
		} catch (IOException ex) {
			ex.printStackTrace();

		}
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
			alllocations = apiService.fetchLocations();
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
		unitWeightLabel.setText("Unit Weight(kN/m3) :");
		unitWeightText = new Text(shell, SWT.BORDER);
		unitWeightText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Water Content field
		Label waterContentLabel = new Label(shell, SWT.NONE);
		waterContentLabel.setText("Water Content(%):");
		waterContentText = new Text(shell, SWT.BORDER);
		waterContentText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		// Shear Strength field
		Label shearStrengthLabel = new Label(shell, SWT.NONE);
		shearStrengthLabel.setText("Shear Strength(kPa) :");
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
		saveButton.addListener(SWT.Selection, e -> addSample(sample, shell, parent));
		cancelButton.addListener(SWT.Selection, e -> shell.close());
		shell.pack();
		shell.open();
	}

	private Object addSample(Sample sample, Shell shell, Composite parent) {
		try {
			LocalDate dateCollected = LocalDate.of(dateCollectedPicker.getYear(), dateCollectedPicker.getMonth() + 1,
					dateCollectedPicker.getDay());
			Double unitWeight = Double.parseDouble(unitWeightText.getText());
			Double waterContent = Double.parseDouble(waterContentText.getText());
			Double shearStrength = Double.parseDouble(shearStrengthText.getText());
			
			// Validation
	        if (waterContent < 5.0 || waterContent > 150.0) {
	            showError(shell, "Water Content must be between 5% and 150%.");
	            return null;
	        }
	        if (unitWeight < 12.0 || unitWeight > 26.0) {
	            showError(shell, "Unit Weight must be between 12 kN/m³ and 26 kN/m³.");
	            return null;
	        }
	        if (shearStrength < 2.0 || shearStrength > 1000.0) {
	            showError(shell, "Shear Strength must be between 2 kPa and 1000 kPa.");
	            return null;
	        }

			sample = new Sample(new Location(selectedLocation.getId(), selectedLocation.getName()), dateCollected,
					unitWeight, waterContent, shearStrength);

			apiService.addSample(sample);
			shell.close();
			loadSamples(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void showError(Shell shell, String message) {
		Shell errorShell = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	    errorShell.setText("Input Error");
	    errorShell.setLayout(new GridLayout(1, false));
	    
	    Label errorMessage = new Label(errorShell, SWT.NONE);
	    errorMessage.setText(message);
	    
	    Button okButton = new Button(errorShell, SWT.PUSH);
	    okButton.setText("OK");
	    okButton.addListener(SWT.Selection, e -> errorShell.close());
	    
	    errorShell.pack();
	    errorShell.open();
		
	}

	private void loadSamples(Composite parent) {
		if (table.isDisposed())
			return;

		// Dispose all existing TableEditors first
		for (TableItem item : table.getItems()) {
			TableEditor editEditor = (TableEditor) item.getData("editEditor");
			TableEditor deleteEditor = (TableEditor) item.getData("deleteEditor");

			if (editEditor != null) {
				if (editEditor.getEditor() != null && !editEditor.getEditor().isDisposed()) {
					editEditor.getEditor().dispose();
				}
				editEditor.dispose();
			}

			if (deleteEditor != null) {
				if (deleteEditor.getEditor() != null && !deleteEditor.getEditor().isDisposed()) {
					deleteEditor.getEditor().dispose();
				}
				deleteEditor.dispose();
			}
		}

		table.removeAll();

		try {
			List<Sample> samples = apiService.fetchSamples();
			for (Sample sample : samples) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] { String.valueOf(sample.getId()), sample.getLocation().getName(),
						sample.getDateCollected().toString(), sample.getUnitWeight().toString(),
						sample.getWaterContent().toString(), sample.getShearStrength().toString() });

				if (!item.isDisposed()) {
					deleteHyperLink(item, sample.getId(), 7, parent);
					editHyperlink(item, 6, parent);
				} else {
					item.dispose();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		parent.layout(true, true);
		table.redraw();
		table.update();
	}

	private void loadThresholdSamples(Composite parent) {
	    Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
	    shell.setText("Threshold Samples");
	    shell.setLayout(new GridLayout(1, false));
	    
	    Table thresholdTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
	    thresholdTable.setHeaderVisible(true);
	    thresholdTable.setLinesVisible(true);
	    thresholdTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    // Define Columns
	    String[] columnNames = { "Id", "Location Name", "Date Collected", "Unit Weight(kN/m3)", "Water Content(%)", "Shear Strength(kPa)" };
	    int[] columnWidths = { 50, 150, 150, 150, 150, 150 };
	    
	    for (int i = 0; i < columnNames.length; i++) {
	        TableColumn column = new TableColumn(thresholdTable, SWT.NONE);
	        column.setText(columnNames[i]);
	        column.setWidth(columnWidths[i]);
	    }
	    
	    try {
	        List<Sample> samples = apiService.fetchThresholdSamples();
	        for (Sample sample : samples) {
	            TableItem item = new TableItem(thresholdTable, SWT.NONE);
	            item.setText(new String[] {
	                String.valueOf(sample.getId()),
	                sample.getLocation().getName(),
	                sample.getDateCollected().toString(),
	                sample.getUnitWeight().toString(),
	                sample.getWaterContent().toString(),
	                sample.getShearStrength().toString()
	            });
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    Button closeButton = new Button(shell, SWT.PUSH);
	    closeButton.setText("Close");
	    closeButton.setLayoutData(new GridData(SWT.CENTER, SWT.BOTTOM, false, false));
	    closeButton.addListener(SWT.Selection, e -> shell.close());
	    
	    shell.pack();
	    shell.open();
	}

	public void deleteHyperLink(TableItem item, int id, int columnIndex, Composite parent) {
		TableEditor deleteEditor = new TableEditor(table);
		Link link = new Link(table, SWT.NONE);
		link.setText("<a>Delete</a>"); // Displayed as hyperlink
		link.addListener(SWT.Selection, e -> {
			try {
				apiService.deleteSample(id);
				Display.getDefault().asyncExec(() -> {
					if (!item.isDisposed()) {
						TableEditor editEditor = (TableEditor) item.getData("editEditor");
						TableEditor deleteEditorStored = (TableEditor) item.getData("deleteEditor");

						if (editEditor != null) {
							if (editEditor.getEditor() != null && !editEditor.getEditor().isDisposed()) {
								editEditor.getEditor().dispose();
							}
							editEditor.dispose();
						}

						if (deleteEditorStored != null) {
							if (deleteEditorStored.getEditor() != null
									&& !deleteEditorStored.getEditor().isDisposed()) {
								deleteEditorStored.getEditor().dispose();
							}
							deleteEditorStored.dispose();
						}
						item.dispose();
						loadSamples(parent);
					}
				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		deleteEditor.grabHorizontal = true;
		deleteEditor.setEditor(link, item, columnIndex);
		item.setData("deleteEditor", deleteEditor); // Store editor reference
	}

	public void editHyperlink(TableItem item, int columnIndex, Composite parent) {
		TableEditor editEditor = new TableEditor(table);
		Link link = new Link(table, SWT.NONE);
		if (columnIndex == 6) {
			link.setText("<a>Edit</a>");// Displayed as hyperlink
			link.addListener(SWT.Selection, e -> {
				openEditDialog(item, parent);
			});

		}
		editEditor.grabHorizontal = true;
		editEditor.setEditor(link, item, columnIndex);
		item.setData("editEditor", editEditor);
	}

	private void openEditDialog(TableItem item, Composite parent) {

		Shell shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM);
		shell.setText("Edit Sample");
		shell.setLayout(new GridLayout(2, false));

		new Label(shell, SWT.NONE).setText("Location:");
		ComboViewer locationCombo = new ComboViewer(shell, SWT.DROP_DOWN);
		locationCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		try {
			alllocations = apiService.fetchLocations();
			locationCombo.setContentProvider(new ArrayContentProvider());
			locationCombo.setInput(alllocations);
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		Label dateLabel = new Label(shell, SWT.NONE);
		dateLabel.setText("Date Collected:");
		dateCollectedPicker = new DateTime(shell, SWT.DATE | SWT.DROP_DOWN);
		dateCollectedPicker.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		
		new Label(shell, SWT.NONE).setText("Unit Weight(kN/m3):");
		Text unitWeightText = new Text(shell, SWT.BORDER);
		unitWeightText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(shell, SWT.NONE).setText("Water Content(%):");
		Text waterContentText = new Text(shell, SWT.BORDER);
		waterContentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(shell, SWT.NONE).setText("Shear Strength(kPa):");
		Text shearStrengthText = new Text(shell, SWT.BORDER);
		shearStrengthText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (!item.isDisposed()) {
			locationCombo.getCombo().setText(item.getText(1));
			LocalDate dateCollected = LocalDate.parse(item.getText(2));
			dateCollectedPicker.setYear(dateCollected.getYear());
			dateCollectedPicker.setMonth(dateCollected.getMonthValue() - 1);
			dateCollectedPicker.setDay(dateCollected.getDayOfMonth());
			unitWeightText.setText(item.getText(3));
			waterContentText.setText(item.getText(4));
			shearStrengthText.setText(item.getText(5));
		}

		locationCombo.getCombo().addListener(SWT.Selection, e -> {
			if (!locationCombo.getCombo().isDisposed()) {
				ISelection selection = locationCombo.getSelection();
				if (selection instanceof StructuredSelection) {
					selectedLocation = (Location) ((StructuredSelection) selection).getFirstElement();
				}
			}
		});

		Button saveButton = new Button(shell, SWT.PUSH);
		saveButton.setText("Save");
		saveButton.addListener(SWT.Selection, e -> {
			Integer locationId = getLocationIdByName(locationCombo.getCombo().getText());

			int id = Integer.parseInt(item.getText(0));
			LocalDate newDateCollected = LocalDate.of(dateCollectedPicker.getYear(), dateCollectedPicker.getMonth() + 1,
					dateCollectedPicker.getDay());
			Double newUnitWeight = Double.parseDouble(unitWeightText.getText());
			Double newWaterContent = Double.parseDouble(waterContentText.getText());
			Double newShearStrength = Double.parseDouble(shearStrengthText.getText());
			
			if (newWaterContent < 5 || newWaterContent > 150) {
	            showError(shell, "Water Content must be between 5% and 150%.");
	            return;
	        }
	        if (newUnitWeight < 12 || newUnitWeight > 26) {
	            showError(shell, "Unit Weight must be between 12 kN/m³ and 26 kN/m³.");
	            return;
	        }
	        if (newShearStrength < 2 || newShearStrength > 1000) {
	            showError(shell, "Shear Strength must be between 2 kPa and 1000 kPa.");
	            return;
	        }

			Sample sample = new Sample(id, new Location(locationId, locationCombo.getCombo().getText()),
					newDateCollected, newUnitWeight, newWaterContent, newShearStrength);

			try {
				apiService.editSample(sample);
				shell.close();		
				loadSamples(parent);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addListener(SWT.Selection, e -> shell.close());

		shell.pack();
		shell.open();
	}

	public Integer getLocationIdByName(String locationName) {
		Integer id = alllocations.stream().filter(p -> p.getName().equals(locationName)).map(Location::getId)
				.findFirst().orElse(1);
		return id;
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