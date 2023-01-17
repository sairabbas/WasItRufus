/*
 * Pronto.ai Take Home Assignment
 * Author: Sair Abbas
 * https://www.linkedin.com/in/sairabbas
 * https://sairabbas.com
 * https://github.com/sairabbas
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Example Input - C:\\Users\\JohnDoe\\Desktop\\Directory");
        while(true){
            //Prompt for user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter local git directory: ");
            String git_dir = scanner.nextLine();
            File file = new File(git_dir);
            //Validate user input
            while(!file.exists()){
                System.out.print("Invalid directory, please re-enter: ");
                git_dir = scanner.nextLine();
                file = new File(git_dir);
            }
            //Assess the git directory
            assessGitStatus(git_dir);
        }
    }
    public static void assessGitStatus(String git_dir) throws IOException, InterruptedException {
        //Predefine git commands
        String[] commands = new String[]{"rev-parse --abbrev-ref HEAD", "diff", "show -s --format=%cs HEAD", "show -s --format=%cn HEAD"};
        //Iterate through each git command
        for(int i = 0; i < commands.length; i++){
            String command = String.format("git -C %s %s", git_dir, commands[i]);
            //Execute git command
            Process proc = Runtime.getRuntime().exec(command);
            //Read executed command line output
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = reader.readLine();
            //Print out command statuses
            if(i == 0){
                System.out.println(String.format("active branch: %s", line));
            }
            else if(i == 1){
                boolean localChanges = line == null ? false : true;
                System.out.println(String.format("local changes: %b", localChanges));
            }
            else if(i == 2){
                LocalDate lastWeekDate = LocalDate.now().minusDays(7);
                LocalDate lastCommitDate = LocalDate.parse(line);
                boolean recentCommit = lastCommitDate.isBefore(lastWeekDate) ? false : true;
                System.out.println(String.format("recent commit: %b", recentCommit));
            }
            else if(i == 3){
                boolean blameRufus = line.equals("Rufus");
                System.out.println(String.format("blame Rufus: %b \n", blameRufus));
            }
            //Wait for process object to terminate
            proc.waitFor();
        }
    }
}