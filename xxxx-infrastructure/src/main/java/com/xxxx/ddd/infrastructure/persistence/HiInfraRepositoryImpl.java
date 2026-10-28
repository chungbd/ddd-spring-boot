package com.xxxx.ddd.infrastructure.persistence;

import com.xxxx.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Service;

@Service
public class HiInfraRepositoryImpl implements HiDomainRepository {
    @Override
    public String sayHi(String name) {
        return "Infrastructure: Hello, " + name + "!";
    }
}
