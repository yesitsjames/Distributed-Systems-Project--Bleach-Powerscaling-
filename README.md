Distributed Systems Project

This distributed systems project involves the creation of a database management system that will overall be focused on characters from the fictional show Bleach. It will showcase the characters powerlevel, profession, corps, age, gender, name and ID. CRUD will be performed using this database, allowing the user to create, read, update and delete. This will be done using a HSQLDB database. 
Main Functions: 
Post – Enter the details for a new bar and create it on the server.
Put – Enter the current name of a bar and the name that it will be changed to and the table is updated
Delete - Enter an Id number and it is deleted from the system. 
Get - Return the information a single entity by entering a name. 
Clear – Clears all crud actions filled out so far.
Show all – Shows all Database contents after selecting only one previously
Print to Excel – Creates an CSV file with all the data.
Fill Tables (menu bar option) – Creates a prefab of 3 listings.
Clear Tables (menu bar option) – Removes all data 
Project Info (menu bar option) - Small popup box with student number and name.

<img width="1047" height="601" alt="image" src="https://github.com/user-attachments/assets/1eeb1384-3842-4bad-b01f-1c70ad18a6dd" />
<img width="453" height="226" alt="image" src="https://github.com/user-attachments/assets/ad514bcd-752d-4e17-a238-f7978c8e7a2c" />


Steps for First Time Use: 
1. Run Tomcat Server. 
2. Run Ant. 
3. Run java application through main
4. Menu Bar -> Fill Tables to create prefab
5. Insert whatever is desired
6. Export to Excel to create a CSV file.

Application checks for the existence of a table.
If no table exists already it will create one.



<img width="286" height="267" alt="image" src="https://github.com/user-attachments/assets/65977f06-2247-417c-9f90-79df12f1cb17" />


For database operations for data access.
To retrieve all records: SELECT * from power
To retrieve a specific record by ID: SELECT * from power WHERE id = ?

All data accessed is displayed in a table format. This can be found in the main window and it will show a clear constructed view of all fields required.

INSERT
String query = "INSERT INTO power (id, name, squad, level, position, age, gender) VALUES ( (Whatever is desired for each )   )";

UPDATE
StringBuilder updateQueryBuilder = new StringBuilder("UPDATE power SET ");
updateQueryBuilder.append(" WHERE id = ?");

DELETE
Delete by ID: DELETE FROM power WHERE id = ?
Delete all records: DELETE FROM power


<img width="449" height="278" alt="image" src="https://github.com/user-attachments/assets/37ff941c-dbc4-4a86-88cc-07875b0e0a30" />


For GUI
Alerts:
If the user tries to call a command without entering all the inputs they will receive a error pop up box. This will advise you a solution. 

<img width="525" height="241" alt="image" src="https://github.com/user-attachments/assets/ef20e180-aa9f-41c5-84a8-5285d8b096ce" />


<img width="465" height="230" alt="image" src="https://github.com/user-attachments/assets/939916cf-2645-4e5b-be8a-d12f456ea1f1" />



And of course if successful:

<img width="454" height="282" alt="image" src="https://github.com/user-attachments/assets/696e4a44-53e7-4d59-99b4-a556d005d52f" />


Printing to excel successfully:

<img width="681" height="300" alt="image" src="https://github.com/user-attachments/assets/99d692b9-e6b3-4698-b034-5798d84693b2" />


Thus receiving this CSV file which I will now open in excel:

<img width="588" height="311" alt="image" src="https://github.com/user-attachments/assets/45f34728-fc98-4d58-a2e7-e57404dc7235" />

