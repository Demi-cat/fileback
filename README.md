# fileback
File backup system


list 							-shows the list of profiles.

entry_list <profileName> 				-shows all the individual history levels in a profile.


profile:
	create <name> <history limit> 			-creates a profile
	edit: add_path 					-opens a file explorer to add a path
	      remove_path 				-opens a file explorer to delete a path
	      rename <newName> 				-renames
	      set_history_limit <value> 		-changes the history limit


backup <profileName> <true/false verbose>       	-backs up the files on the profile


rollback: do <profileName> <true/false verbose> 	-puts backed up files back into Live data.
	  undo <profileName> <true/false verbose>	-undoes rollback if valueable data was overwritten by accident.
	  commit <profileName> <true/false verbose>	-permanently applies rollback, deletes temporary snapshot.
