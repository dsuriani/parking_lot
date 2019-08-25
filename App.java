import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	private static String COMMAND_CREATE_PARKING_LOT = "create_parking_lot";
	private static String COMMAND_PARK = "park";
	private static String COMMAND_LEAVE = "leave";
	private static String COMMAND_STATUS = "status";
	private static String COMMAND_SLOT_NUMBERS_FOR_CARS_WITH_COLOUR = "slot_numbers_for_cars_with_colour";
	private static String COMMAND_SLOT_NUMBER_FOR_REGISTRATION_NUMBER = "slot_number_for_registration_number";
	private static String COMMAND_REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR = "registration_numbers_for_cars_with_colour";
	private static Object[] listSlot = null;
	private static List<String> listCommand = new ArrayList<String>();
    private static int manySlot = 0;
    private static int currentSlot = 0;
    
	public static void main( String[] args ) {
    	if(validateParameters(args)) {
    		try {
	    		BufferedReader buffer = new BufferedReader(new FileReader(args[0]));
				String line = "";
				while(line != null) {
					line = buffer.readLine();
					readCommands(line);
				}
		        buffer.close();
		        readParameter();
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    public static boolean validateParameters(String[] args) {
    	if(args != null && args.length == 1) {
    		Map<String, Object> retval = validateFileTxt(args[0]);
    		if ((Boolean)retval.get("validation")) {
        		File file = new File(args[0]);
        		if (file.exists()) {
        			return true;
        		} else {
        			System.out.println("The file is unavailable or the file path is incorrect");
        			return false;
        		}
    		} else {
    			System.out.println("Parameter is invalid");
    			return false;
    		}
    	} else if(args.length > 1) {
    		System.out.println("2 Parameters found, but only acknowledge 1st parameter");
    		File file = new File(args[0]);
    		if (file.exists())
    			return true;
    		else
    			return false;
    	} else {
    		return false;
    	}
    }
    public static Map<String, Object> validateFileTxt(String fileName) {
    	Map<String, Object> retval = new HashMap<String, Object>();
    	if (fileName != null && !fileName.trim().equalsIgnoreCase("")) {
    		if(fileName.contains(".")) {
    			String[] fileType = fileName.split("\\.");
    			if(fileType.length == 1) {
    				retval.put("type", "");
    				retval.put("validation", true);
    				return retval;
    			}
    			if (fileType.length == 2) {
    				retval.put("type", fileType[1]);
    				retval.put("validation", true);
    				return retval;
    			} else {
    				retval.put("type", fileType[1]);
    				retval.put("validation", false);
    				return retval;
    			}
    		} else {
    			retval.put("type", "");
				retval.put("validation", false);
				return retval;
    		}
    	} else {
    		retval.put("type", "");
			retval.put("validation", false);
			return retval;
    	}
    }
    public static void readParameter() {
      String inputReader = "";
      while(!inputReader.equalsIgnoreCase("exit")) {
      	try {
      		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		inputReader = userInput.readLine();
      		readCommands(inputReader);
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
      }
    }
    public static void readCommands(String commands) {
    	if(commands != null) {
        	String mainCommand = "";
        	if(commands.contains(" ")) {
      			String[] commandPrompt = commands.split(" ");
      			if(commandPrompt.length > 0) {
      				mainCommand = commandPrompt[0];
      			} else {
      				mainCommand = commands;
      			}
      			if(mainCommand.equals(COMMAND_CREATE_PARKING_LOT) || mainCommand.equals(COMMAND_LEAVE) ||
   				   mainCommand.equals(COMMAND_SLOT_NUMBERS_FOR_CARS_WITH_COLOUR) ||
   				   mainCommand.equals(COMMAND_SLOT_NUMBER_FOR_REGISTRATION_NUMBER) ||
   				   mainCommand.equals(COMMAND_REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR)) {
   					if(commandPrompt.length == 2) {
   						if(isCommandExist(mainCommand))
							listCommand.add(mainCommand);
   						if(mainCommand.equals(COMMAND_CREATE_PARKING_LOT)) {
   							if(isCommandExist(mainCommand) == false) {
	   							listCommand.add(mainCommand);
   							}
   							if(isStringANumber(commandPrompt[1])) {
	   							manySlot = Integer.parseInt(commandPrompt[1]);
	   							listSlot = new Object[manySlot];
   							}
   						} else if(mainCommand.equals(COMMAND_LEAVE)) {
   							if(!isCommandExist(mainCommand)) {
	   							listCommand.add(mainCommand);
   							}
   							if(isStringANumber(commandPrompt[1])) {
	   							currentSlot = Integer.parseInt(commandPrompt[1]);
	   							listSlot[currentSlot] = null;
	   							System.out.println("Slot number " + (currentSlot+1) + " is free");
   							}
   						} else if(mainCommand.equals(COMMAND_SLOT_NUMBERS_FOR_CARS_WITH_COLOUR)) {
   							if(!isCommandExist(mainCommand)) {
	   							listCommand.add(mainCommand);
   							}
   							executeCommandSlotNumbersForCarsWithColour(commandPrompt[1]);
   		  				} else if(mainCommand.equals(COMMAND_SLOT_NUMBER_FOR_REGISTRATION_NUMBER)) {
   		  					if(!isCommandExist(mainCommand)) {
	   							listCommand.add(mainCommand);
							}
   		  					executeCommandSlotNumberForRegistrationNumber(commandPrompt[1]);
   		  				} else if(mainCommand.equals(COMMAND_REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR)) {
   		  					if(!isCommandExist(mainCommand)) {
   		  						listCommand.add(mainCommand);
   		  					}
   		  					executeCommandRegistrationNumbersForCarsWithColour(commandPrompt[1]);
   		  				}
   					} else {
   						System.out.println("Invalid Command");
   					}
   				} else if(mainCommand.equals(COMMAND_PARK)) {
   					if(commandPrompt.length == 3) {
   						if (isCommandExist(mainCommand) == false) {
   							listCommand.add(mainCommand);
   						}
   						if(checkSlot()) {
	   						Map<String, Object> data = new HashMap<String, Object>();
	   						data.put("registration", commandPrompt[1]);
	   						data.put("color", commandPrompt[2]);
	   						listSlot[currentSlot] = data;
	   						System.out.println("Allocated slot number:" + (currentSlot+1));
	   						currentSlot++;
   						} else {
   							System.out.println("Sorry, parking lot is full");
   						}
   					} else {
   						System.out.println("Invalid Command " + mainCommand);
   					}
   				} else {
   					System.out.println("Invalid command(s)");
   				}
      		} else {
      			mainCommand = commands;
      			if(mainCommand.equals(COMMAND_STATUS)) {
   					System.out.println("status");
   					if(!isCommandExist(mainCommand))
						listCommand.add(mainCommand);
					executeCommandStatus();
   				}
			else if(mainCommand.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
      		}
    	} else {
    		System.out.println("No command found");
    	}
    }
    private static boolean isStringANumber(String s) {
    	try {
    		Integer.parseInt(s);
    		return true;
    	} catch(NumberFormatException e) {
    		return false;
    	}
    }
    protected static boolean isCommandExist(String commandPrompt) {
    	if(listCommand.size() == 0) {
    		return false;
    	} else {
    		boolean retval = false;
    		for(int i = 0; i < listCommand.size(); i++) {
    			if(listCommand.get(i).toString().equals(commandPrompt)) {
    				retval = true;
    				break;
    			} else if(listCommand.get(i).toString().equals(commandPrompt) == false &&
    					  i == (listCommand.size() - 1)) {
    				retval = false;
    				break;
    			}
    		}
    		return retval;
    	}
    }
    protected static boolean checkSlot() {
    	boolean retval = false;
    	if(listSlot != null && listSlot.length > 0) {
    		for(int i = 0; i < listSlot.length; i++) {
    			if(listSlot[i] == null) {
    				retval = true;
    				break;
    			}
    		}
    	}
    	return retval;
    }
    protected static void executeCommandStatus() {
    	if(listSlot != null && listSlot.length > 0) {
			System.out.println("Slot No.\t\tRegistrationNo.\t\tColour");
    		for(int i = 0; i < listSlot.length; i++) {
    			if(listSlot[i] != null) {
	    			Map<String, Object> data = (Map<String, Object>)listSlot[i];
	    			System.out.println((i+1)+"\t\t\t"+data.get("registration").toString()+"\t\t"+data.get("color").toString());
    			}
    		}
    	}
    }
    protected static List<String> executeCommandSlotNumbersForCarsWithColour(String color) {
    	List<String> retval = new ArrayList<String>();
    	if(listSlot != null && listSlot.length > 0) {
    		for(int i = 0; i < listSlot.length; i++) {
    			if(listSlot[i] != null) {
	    			Map<String, Object> data = (Map<String, Object>)listSlot[i];
	    			if(data.get("color").toString().equals(color)) {
	    				retval.add(String.valueOf(i+1));
	    			}
    			}
    		}
    	}
    	printList(retval);
    	return retval;
    }
    protected static List<String> executeCommandRegistrationNumbersForCarsWithColour(String color) {
    	List<String> listRegistration = new ArrayList<String>();
    	if(listSlot != null && listSlot.length > 0) {
    		for(int i = 0; i < listSlot.length; i++) {
    			if(listSlot[i] != null) {
	    			Map<String, Object> data = (Map<String, Object>)listSlot[i];
	    			if(data.get("color").toString().equals(color)) {
	    				listRegistration.add(data.get("registration").toString());
	    			}
    			}
    		}
    	}
    	printList(listRegistration);
    	return listRegistration;
    }
    protected static List<String> executeCommandSlotNumberForRegistrationNumber(String number) {
    	List<String> listRegistration = new ArrayList<String>();
    	if(listSlot != null && listSlot.length > 0) {
    		for(int i = 0; i < listSlot.length; i++) {
    			if(listSlot[i] != null) {
	    			Map<String, Object> data = (Map<String, Object>)listSlot[i];
	    			if(data.get("registration").toString().equals(number)) {
	    				listRegistration.add(String.valueOf(i));
	    			}
    			}
    		}
    	}
    	printList(listRegistration);
    	return listRegistration;
    }
    protected static void printList(List<String> list) {
    	if(list != null && list.size() > 0) {
    		for(int i = 0; i < list.size(); i++) {
    			if (i < list.size() - 1)
    				System.out.print("" + list.get(i).toString() + ",");
    			else
    				System.out.print("" + list.get(i).toString());
    		}
    	}
    	System.out.println("");
    }
}
