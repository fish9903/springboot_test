package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item",item);

        return "basic/item";
    }

//    @PostMapping("/add") // 새로고침 문제
    public String addItemV2(@ModelAttribute("item") Item item) {
/*        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);*/
        // -> @ModelAttribute 가 자동으로 만들어 줌

        itemRepository.save(item);
//        model.addAttribute("item",item); // -> @ModelAttribute 의 파라미터 이름으로 추가됨

        return "basic/item";
    }

//    @PostMapping("/add") // 저장 되었다는 알람 띄우기 위해 V4로 이동
    public String addItemV3(@ModelAttribute("item") Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId()); // {itemId}로 들어감
        redirectAttributes.addAttribute("status", true); // 쿼리 파라미터로 됨

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute("item") Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/deleteForm";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam MultiValueMap<String, List<Long>> paramMap, Model model) {
        ArrayList<Long> values = new ArrayList(paramMap.get("checked"));

        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            itemRepository.delete(Long.parseLong((String) iter.next()));
        }

        return "redirect:/basic/items";
    }

    /**
     * 테스트용 데이터 추가
     */
//    @PostConstruct
//    public void init() {
//        itemRepository.save(new Item("itemA", 10000,100));
//        itemRepository.save(new Item("itemB", 20000,200));
//    }
}
