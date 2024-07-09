package com.myplantsdiary.myplantsdiaryapi.service;

import com.myplantsdiary.myplantsdiaryapi.domain.Cart;
import com.myplantsdiary.myplantsdiaryapi.domain.CartItem;
import com.myplantsdiary.myplantsdiaryapi.domain.Member;
import com.myplantsdiary.myplantsdiaryapi.domain.Product;
import com.myplantsdiary.myplantsdiaryapi.dto.CartItemDTO;
import com.myplantsdiary.myplantsdiaryapi.dto.CartItemListDTO;
import com.myplantsdiary.myplantsdiaryapi.repository.CartItemRepository;
import com.myplantsdiary.myplantsdiaryapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int quantity = cartItemDTO.getQuantity();
        Long cino = cartItemDTO.getCino();

        //기존에 상품이 담겨있는 경우 (수량만 변경)
        if(cino != null){
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
            CartItem cartItem = cartItemResult.orElseThrow();
            cartItem.changeQuantity(quantity);
            cartItemRepository.save(cartItem);
            return getCartItems(email);

        }else{

            Cart cart = getCart(email);
            CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);
            if(cartItem == null){
                Product product = Product.builder().pno(pno).build();

                cartItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(quantity)
                        .build();
            }else{
                cartItem.changeQuantity(quantity);
            }
            cartItemRepository.save(cartItem);
        }

        return getCartItems(email);
    }

    private Cart getCart(String email) {
        Cart cart = null;
        Optional<Cart> result = cartRepository.getCartOfMember(email);
        if(result.isEmpty()){ //없으면 Cart 객체 생성하고 추가 변환.
            log.info("Cart of the member is not exist!");
            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();
            cart = cartRepository.save(tempCart);
        }else{
            //해당 이메일의 장바구니가 있는 경우.
            cart = result.get();
        }
        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {
        Long cno = cartItemRepository.getCartFromItem(cino);
        cartItemRepository.deleteById(cino);
        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }
}
