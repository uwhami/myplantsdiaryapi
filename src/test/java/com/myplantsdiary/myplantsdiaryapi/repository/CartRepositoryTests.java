package com.myplantsdiary.myplantsdiaryapi.repository;

import com.myplantsdiary.myplantsdiaryapi.domain.Cart;
import com.myplantsdiary.myplantsdiaryapi.domain.CartItem;
import com.myplantsdiary.myplantsdiaryapi.domain.Member;
import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.dto.CartItemListDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void testListOfMember(){

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemListDTOS =  cartItemRepository.getItemsOfCartDTOByEmail(email);

        for(CartItemListDTO cartItemListDTO : cartItemListDTOS){
            log.info(cartItemListDTO);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct(){
        String email = "user1@aaa.com";
        Long pno = 6L;
        int quantity = 3;

        //이메일과 상품번호로 장바구니 아이템 확인 -> 없으면 추가, 있으면 수량 변경해서 저장.

        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        if(cartItem != null) {
            cartItem.changeQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
            return;
        }

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        if(result.isEmpty()){
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        }else{
            cart = result.get();
        }
        Product product = Product.builder().pno(pno).build();

        cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();

        cartItemRepository.save(cartItem);

    }


    @Transactional
    @Commit
    @Test
    public void testUpdateByCino(){
        Long cino = 1L;
        int quantity = 9;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();
        cartItem.changeQuantity(quantity);
        cartItemRepository.save(cartItem);

    }

    @Test
    @Commit
    public void testDeleteThenList(){
        Long cino = 2L;
        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cino);
        List<CartItemListDTO> items = cartItemRepository.getItemsOfCartDTOByCart(cno);
        for(CartItemListDTO cartItemListDTO : items){
            log.info(cartItemListDTO);
        }
    }


}
