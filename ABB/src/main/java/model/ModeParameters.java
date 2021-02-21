package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModeParameters {
    public CalculationType type;
    //public List<SchemeRegim>regim = new ArrayList<>();
    public  SchemeRegim regim;
    public List<SchemeElement>elements = new ArrayList<>();
}
