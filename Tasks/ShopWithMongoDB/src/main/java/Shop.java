import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Shop {
    private String name;
    private List<Goods> goods;
}
