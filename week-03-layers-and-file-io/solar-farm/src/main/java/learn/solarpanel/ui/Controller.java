package learn.solarpanel.ui;

import learn.solarpanel.data.DataAccessException;
import learn.solarpanel.domain.SolarResult;
import learn.solarpanel.domain.SolarService;
import learn.solarpanel.models.SolarPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Controller {

    private final View view;
    private final SolarService service;

    @Autowired
    public Controller(View view, SolarService service){
        this.view=view;
        this.service=service;
    }

    public void run(){
        try{
            runMenu();
        } catch (DataAccessException ex){
            view.printHeader("Fatal Error: " + ex);
        }

    }

    public void runMenu() throws DataAccessException {
        view.printHeader("Welcome to Solar Farm");
        MenuOptions option;
        do {
            option = view.printMenuAndSelect();
            switch (option) {
                case EXIT:
                    view.printHeader("Goodbye");
                    break;
                case DISPLAY_PANELS_BY_SECTION:
                    findPanelsBySection();
                    break;
                case ADD_PANEL:
                    addPanel();
                    break;
                case UPDATE_PANEL:
                    updatePanel();
                    break;
                case REMOVE_PANEL:
                    removePanel();
                    break;
            }
        }while (option!=MenuOptions.EXIT);

    }

    public void findPanelsBySection() throws DataAccessException {
        view.printHeader(MenuOptions.DISPLAY_PANELS_BY_SECTION.getTitle());
        String section = view.getSection(findAllSections());
        if(section==null){
            return;
        }
        List<SolarPanel> panels = service.findBySection(section);
        view.printSolarPanels(panels, section);
    }

    public void addPanel() throws DataAccessException{
        view.printHeader(MenuOptions.ADD_PANEL.getTitle());
        SolarPanel panel = view.createPanel();
        SolarResult result;
        boolean exit;
        do {
            exit=true;
            result = service.add(panel);
            view.printResult(result);
            if(!result.isSuccess() && result.getMessages().get(0).toLowerCase().contains("duplicate")){
                panel= view.updateSectRowCol(panel);
                exit = false;
            }
        }while(!exit);
    }

    public void updatePanel() throws DataAccessException {
        view.printHeader(MenuOptions.UPDATE_PANEL.getTitle());
        String section = view.getSection(findAllSections());
        if(section==null){
            return;
        }
        int row = view.getRow(true);
        int col = view.getCol(true);
        SolarPanel panel = service.findOne(section, row, col);
        panel = view.updatePanel(panel);
        if(panel==null){
            return;
        }
        SolarResult result;
        boolean exit;
        do {
            exit=true;
            result = service.update(panel);
            view.printResult(result);
            if(!result.isSuccess() && result.getMessages().get(0).toLowerCase().contains("duplicate")){
                panel= view.updateSectRowCol(panel);
                exit = false;
            }
        }while(!exit);
    }

    public void removePanel() throws DataAccessException {
        view.printHeader(MenuOptions.REMOVE_PANEL.getTitle());
        String section = view.getSection(true);
        int row = view.getRow(true);
        int col = view.getCol(true);
        SolarResult result = service.delete(section, row, col);
        view.printResult(result);
    }

    private List<String> findAllSections() throws DataAccessException{
        List<SolarPanel> all = service.findAll();
        List<String> sections = new ArrayList<>();
        for(SolarPanel p: all){
            if(!sections.contains(p.getSection().toLowerCase())){
                sections.add(p.getSection().toLowerCase());
            }
        }
        return sections;
    }
}
