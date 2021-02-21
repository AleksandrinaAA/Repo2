package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchemeElement {


    public int numberNode;
    //private int endNode = -1;
    public String parametrName;
    public double value;
    //private double multiplier;
}
