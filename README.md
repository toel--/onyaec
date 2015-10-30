# onyaec
Oh no! Yet another Excel converter!

Converts text files to Excel files and vice versa.
Uses Apache POI to handle the Excel files
Requires Java 8 or greater.


Syntax:
  java -jar onyaec [params] [src] [dst]
where:
  [params] to override the value in the config.ini file. ex: -encoding:UTF-8 -boldFirstRow:true -freezeFirstRow:true
  [src] the source file
  [dst] the destination file

The default configuration is saved in the config.ini file.


Usage example:

Convert tab separated txt file to Excel 97:
java -jar onyaec.jar path/to/file.txt path/to/excel.xls

Convert comma separated txt file to Excel:
java -jar onyaec.jar -separator:comma path/to/file.txt path/to/excel.xlsx

Convert Excel file to tab separated txt file in ISO-8859-1:
java -jar onyaec.jar path/to/excel.xlsx -encoding:ISO-8859-1 path/to/file.txt 


Toël Hartmann
