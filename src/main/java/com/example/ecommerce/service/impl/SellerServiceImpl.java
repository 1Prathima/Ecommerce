package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.SellerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.SellerResponseDto;
import com.example.ecommerce.Exceptions.EmailAlreadyPresentException;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import com.example.ecommerce.Transformer.SellerTransformer;
import com.example.ecommerce.entity.Seller;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    SellerRepository sellerRepository;

    @Override
    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) throws EmailAlreadyPresentException {
//        Seller seller = new Seller();
//        seller.setName(sellerRequestDto.getName());
//        seller.setAge(sellerRequestDto.getAge());
//        seller.setMobNo(sellerRequestDto.getMobNo());
//        seller.setEmailId(sellerRequestDto.getEmailId());

        //write builder in the class for which you want to create object
//        Seller seller = Seller.builder()
//                .name(sellerRequestDto.getName())
//                .age(sellerRequestDto.getAge())
//                .mobNo(sellerRequestDto.getMobNo())
//                .emailId(sellerRequestDto.getEmailId())
//                .build();

        if(sellerRepository.findByEmailId(sellerRequestDto.getEmailId())!=null){
            throw new EmailAlreadyPresentException("Email Id is already registered");
        }

        Seller seller = SellerTransformer.SellerRequestDtoToSeller(sellerRequestDto);
        Seller savedSeller = sellerRepository.save(seller);

        SellerResponseDto sellerResponseDto = SellerTransformer.SellerToSellerResponseDto(savedSeller);
        return sellerResponseDto;
    }

    @Override
    public SellerResponseDto getSellerByEmail(String emailId) throws InvalidSellerException {
        Seller seller;
        try{
            seller = sellerRepository.findByEmailId(emailId);
        }
        catch(Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }

        return SellerTransformer.SellerToSellerResponseDto(seller);
    }

    @Override
    public SellerResponseDto getSellerById(int id) throws InvalidSellerException {
        Seller seller;
        try{
            seller = sellerRepository.findById(id).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }

        return SellerTransformer.SellerToSellerResponseDto(seller);
    }

    @Override
    public List<SellerResponseDto> getAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();

        List<SellerResponseDto> sellerResponseDtos = new ArrayList<>();
        for(Seller seller : sellers){
            sellerResponseDtos.add(SellerTransformer.SellerToSellerResponseDto(seller));
        }

        return sellerResponseDtos;
    }

    @Override
    public List<SellerResponseDto> getSellersByAge(int age) {
        List<Seller> sellers = sellerRepository.findAll();

        List<SellerResponseDto> sellerResponseDtos = new ArrayList<>();
        for(Seller seller : sellers){
            if(seller.getAge() > age){
                sellerResponseDtos.add(SellerTransformer.SellerToSellerResponseDto(seller));
            }
        }
        return sellerResponseDtos;
    }

    @Override
    public void update(String emailId, String name) throws InvalidSellerException {
        Seller seller;
        try{
            seller = sellerRepository.findByEmailId(emailId);
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }

        seller.setName(name);
        sellerRepository.save(seller);
    }

    @Override
    public void deleteByEmail(String emailId) throws InvalidSellerException {
        try{
//            sellerRepository.deleteByEmailId(emailId);  //did not work
            Seller seller = sellerRepository.findByEmailId(emailId);
            sellerRepository.delete(seller);
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }
    }

    @Override
    public void deleteById(int id) throws InvalidSellerException {
        try{
            sellerRepository.deleteById(id);
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }
    }
}
