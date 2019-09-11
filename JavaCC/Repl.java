import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class Repl {

	Scope environment;
	
	public Repl() {
		environment = new Scope();
	}
	
	public void start() {
		System.out.println("Welcome to the ForteLang repl (v0.0.2). Type :help for help.");
		System.out.println();
		Scanner scanner = ForteLang.getGlobalScanner();
		repl: while(true) {
		  	System.out.print("fl-repl> ");
			String input = null;
			try { 
				input = scanner.nextLine();
			} catch(Exception e) { scanner.close(); }
			switch(input) {
				case ":help":
					System.out.println("The following commands are available:");
					System.out.println();
					System.out.println("  :q\tExit ForteLang repl");
					System.out.println("  :e\tDisplay current declared variables");
					System.out.println();
					continue repl;
				case ":q":
					break repl;
				case ":e":
					System.out.println(environment);
					continue repl;
			}
			try { 
				
				if(input.matches("[A-Za-z0-9_']+.*=[^=]*")) {
					String varName = input.split("=")[0].trim();
					Object result = evaluate(input.split("=")[1]);
					
					environment.put(varName, result);
					System.out.println("Added " + ForteLang.prettifyOutput(result) + " to environment");
				} else {
					Object result = evaluate(input);
					System.out.println("=> " + ForteLang.prettifyOutput(result) + "\n");
				}
			} catch(Throwable t) {
				System.out.println("=>> Error, invalid input \"" + input + "\"");
				System.out.println(t.getMessage() + "\n");
			}
		}
	}
	
	private Object evaluate(String input) throws ParseException, Exception {
		ForteLang forteLang = new ForteLang(new ByteArrayInputStream(input.getBytes()));
		return forteLang.input(environment);
	}
	
}
