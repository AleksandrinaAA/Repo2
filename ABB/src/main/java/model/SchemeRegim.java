package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemeRegim  {

    public String regimTemplate;
    public String ancTemplate;
    public String transTemplate;
    public String regim;
    public String anc;
    public String trans;
    public String losses;

}
