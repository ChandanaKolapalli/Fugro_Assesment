# Offshore Ground Sampling Management System(Frontend)

## Overview
This is a full-stack application to manage offshore ground sampling data for environmental studies. It allows users to create, retrieve and delete samples from the Eclipse RCP application.

## Features
- Displays sampling data in a table format.
- Supports adding,editing and deleting samples.
- Calls backend REST APIs to manage data.
- Displays the Average Water Content for the samples
- Displays the Threshold Values that supresses the configured values in the backend
- Basic validation is added


## Prerequisites
Make sure you have the following installed:
- Java 21 or later
- An Eclipse IDE 

## Getting Started

### Clone the Repository
```sh
git clone https://github.com/ChandanaKolapalli/Fugro_Assesment.git
cd Fugro_Assesment_Frontend
```

##Setup & Installation

1. Import Eclipse RCP Project

- Open Eclipse IDE.
- Go to File > Import > Existing Projects into Workspace.
- Select the frontend project folder.
- Click Finish.

2. Run Eclipse RCP Application

- Right-click on the project.
- Select Run As > Eclipse Application.

3. Using the Application

- The main window displays all sample data.
- Click "Add Sample" to insert new records.
- Select a sample and use "Delete" button.
- Select a sample and use "Edit" button.
- Click on "Get Avg Water Content" button to check the Average water content of all samples.
- Click on "Get Threshold Samples" button to get the samples surpassing threshold values 
