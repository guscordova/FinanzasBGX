package jsf;

import entities.Modelo;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import session.ModeloFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("modeloController")
@SessionScoped
public class ModeloController implements Serializable {

    private List<Modelo> models = null;
    @EJB
    private session.ModeloFacade ejbFacade;
   
    public ModeloController() {
    }
    
    private ModeloFacade getFacade() {
        return ejbFacade;
    }
    
    public List<Modelo> getModels() {
        models = getFacade().findAll();
        return models;
    }
}
