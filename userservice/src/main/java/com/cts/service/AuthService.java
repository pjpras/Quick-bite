package com.cts.service;

import com.cts.dto.*;

public interface AuthService {
    public RegisterCustomerResponseDTO createCustomer(RegisterCustomerRequestDTO registerRequestDTO);
    public LoginResponseDTO loginUser(LoginRequestDTO loginDTO);
    public RegisterDeliveryPartnerResponseDTO createDeliveryPartner(RegisterDeliveryPartnerRequestDTO registerDeliveryPartnerDTO);
}
