# Shortest paths finder
##### *JAVA application*

This is a program that finds all possible shortest paths in a map (graph). 
Map is provided as a XML file with a strict content format. After calculation of all shortest paths, program generates an output JSON file that contains information about runtime in milliseconds and list of all shortest paths.

XML file content should be strictly formatted and should contain only the following tags:
- map
- cells
- cell
- start-point
- end-point

Example:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<map>
	<cells>
		<cell row="1" col= "A" />
		<cell row="1" col= "B" />
		<cell row="1" col= "C" />
		<cell row="1" col= "D" />
		<cell row="2" col= "B" />
		<cell row="3" col= "A" />
		<cell row="3" col= "B" />
		<cell row="3" col= "C" />
		<cell row="3" col= "D" />		
		<cell row="4" col= "A" />
		<cell row="4" col= "C" />
		<cell row="4" col= "D" />		
	</cells>	
	<start-point row="1" col= "C" />
	<end-point row="4" col= "C" />
</map>
```

## How to run?

You can run this program through CLI or any JAVA IDE.
This program has a mandatory parameter which is a path to the XML input file that represents a map (graph) and an optional parameter which is destination path for the output JSON file. 
You can provide absolute or relative path to the file. If path contains any spaces, use quote marks.
This repository has an executable jar file in the **exe_jar** folder.

Examples:
```sh
$ java -jar <path_to_jar_executable>/<file_name>.jar "<path_to_xml_file/<file_name>.xml" "<destination_path_for_output_file>"
```

```sh
$ java -jar <path_to_jar_executable>/<file_name>.jar "<path_to_xml_file/<file_name>.xml"
```

