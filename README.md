# ilet
Integrated log excerpt timeline

Returns a filtered log timeline from multiple logfiles. User should create json config files for each log file
which describes the "marker words" which determines which lines are filtered from the logfile. 

- In the result the lines are represented by "timeline keywords". 
- The timestamp in the log should be fixed length the position should be set in json.
- countFromFirstToLast not implemented yet
- column name and column index is the header column name and column order

Known limitations:
- logfiles are loaded one by one into an in-memory collection.
- whole result which is returned also stored in-memory
- everything was implemented the most basic way, so there is no guarantee that this solution will be useful in you use case.  

