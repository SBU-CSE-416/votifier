package me.Votifier.server.services;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.MalformedURLException;

import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.exceptions.UnknownFileException;

@Service
public class DataService {
    
}