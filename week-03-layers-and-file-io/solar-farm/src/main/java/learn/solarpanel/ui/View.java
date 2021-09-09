package learn.solarpanel.ui;

import learn.solarpanel.domain.SolarResult;
import learn.solarpanel.models.SolarPanel;
import learn.solarpanel.models.MaterialType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class View {
    private final ConsoleIO cs = new ConsoleIO();

    public void printHeader(String text){
        System.out.println();
        System.out.println(text);
        System.out.println("=".repeat(text.length()));
        System.out.println();
    }

    public MenuOptions printMenuAndSelect(){
        MenuOptions[] values = MenuOptions.values();
        System.out.println();
        System.out.println("*".repeat(13));
        System.out.println("  MAIN MENU  ");
        System.out.println("*".repeat(13));

        for(int i=0; i<values.length; i++){
            System.out.printf("%s. %s%n", i+1, values[i].getTitle());
        }
        int index = cs.readInt("Select [1-5]:",1,5, true);
        return values[index-1];

    }

    public void printSolarPanels(List<SolarPanel> panels, String section){
        String format = "%3s %3s %4s %8s %8s %n";
        System.out.println("\nPanels in " + section);
        System.out.println("Row Col Year Material Tracking");
        for(SolarPanel p : panels){
            System.out.printf(format, p.getRow(), p.getCol(), p.getYearInstalled()
                    , p.getMaterial().getName(), p.getTracking() ? "yes" : "no");
        }
    }

    public void printResult(SolarResult result){
        if(result.isSuccess()){
            System.out.println("\n[Success]");
            SolarPanel panel = result.getPanel();
            System.out.printf("Panel %s-%s-%s %s.%n", panel.getSection(),
                    panel.getRow(), panel.getCol(), result.getAction());
        }else {
            System.out.println("\n[Err]");
            for(String err : result.getMessages()){
                System.out.println(err);
            }
        }
    }

    public SolarPanel createPanel(){
        SolarPanel panel = new SolarPanel();
        panel.setSection(getSection(true));
        panel.setRow(getRow(true));
        panel.setCol(getCol(true));
        panel.setMaterial(getMaterial("Material: ", true));
        panel.setYearInstalled(cs.readInt("Installation Year: ",1,2021,true));
        panel.setTracking(cs.readBoolean("Tracked [y/n]: ", true));
        return panel;
    }

    public SolarPanel updatePanel(SolarPanel panel){
        if(panel==null){
            System.out.println("Panel is not found.");
            return null;
        }
        System.out.printf("%nEditing %s-%s-%s%nPress [Enter] to keep original value.%n%n",
                panel.getSection(), panel.getRow(), panel.getCol());

        // update section, row, col
        panel = updateSectRowCol(panel);

        // update material
        MaterialType material = getMaterial(
                createPrompt("Material", panel.getMaterial().getName()), false);
        if(material!=null){
            panel.setMaterial(material);
        }

        //update year
        int year = cs.readInt(createPrompt("Installation Year",
                        Integer.toString(panel.getYearInstalled())),1,2021,false);
        if(year!=0){
            panel.setYearInstalled(year);
        }

        // update tracking
        String tracking = panel.getTracking() ? "yes" : "no";
        Boolean isTracking = cs.readBoolean(
                createPrompt("Tracked", tracking, "[y/n]"), false);
        if(isTracking!=null){
            panel.setTracking(isTracking);
        }

        return panel;
    }

    public SolarPanel updateSectRowCol(SolarPanel panel){
        //update section
        String section = getSection(false,panel.getSection());
        if (section!=null && !section.isBlank()){
            panel.setSection(section);
        }

        //update row
        int row = getRow(false, panel.getRow());
        if(row!=0){
            panel.setRow(row);
        }

        //update col
        int col = getCol(false, panel.getCol());
        if(col!=0){
            panel.setRow(col);
        }

        return panel;
    }

    public int getRow(boolean isRequired){
        return cs.readInt("Row: ", 1, 250, isRequired);
    }

    public int getRow(boolean isRequired, int data){
        return cs.readInt(createPrompt("Row", Integer.toString(data)), 1, 250, isRequired);
    }

    public int getCol(boolean isRequired){
        return cs.readInt("Column: ", 1, 250, isRequired);
    }

    public int getCol(boolean isRequired, int data){
        return cs.readInt(createPrompt("Column", Integer.toString(data)), 1, 250, isRequired);
    }

    public String getSection(boolean isRequired){
        return cs.readString("Section: ", isRequired);
    }

    public String getSection(boolean isRequired, String data){
        return cs.readString(createPrompt("Section", data), isRequired);
    }

    public String getSection(List<String> sections){
        if(sections.size()==0){
            System.out.println("There is no existing panel.");
            return null;
        }
        System.out.println("Sections:");
        for(int i=0; i<sections.size();i++){
            String section = sections.get(i).substring(0,1).toUpperCase() + sections.get(i).substring(1);
            System.out.printf("%s. %s%n", i+1, section);
        }
        System.out.printf("%s. Exit (Go back to main menu)%n", sections.size()+1);
        int index = cs.readInt("Select [1-"+(sections.size()+1)+"]:",1,sections.size()+1, true);
        if(index==sections.size()+1){
            return null;
        }
        return sections.get(index-1).substring(0,1).toUpperCase() + sections.get(index-1).substring(1);
    }

    public MaterialType getMaterial(String prompt, boolean isRequired){
        System.out.println(prompt);
        MaterialType[] values = MaterialType.values();

        for(int i=0; i<values.length; i++){
            System.out.printf("%s. %s%n", i+1, values[i].getName());
        }
        int index = cs.readInt("Select [1-5]:",1,5, isRequired);
        if(index==0){
            return null;
        }
        return values[index-1];
    }

    private String createPrompt(String msg, String data){
        return msg + " ("+data+"): ";
    }

    private String createPrompt(String msg, String data1, String data2){
        return msg + " ("+data1+") " + data2 + ": ";
    }


}
