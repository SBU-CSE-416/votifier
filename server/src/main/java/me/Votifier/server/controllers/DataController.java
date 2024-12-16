package me.Votifier.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.TypeReference;

import me.Votifier.server.services.DataService;
import me.Votifier.server.model.EconomicGroup;
import me.Votifier.server.model.RacialGroup;
import me.Votifier.server.model.StateAbbreviation;
import me.Votifier.server.model.RegionType;
import me.Votifier.server.model.repository.StateSummaryRepository;
import me.Votifier.server.model.repository.DistrictsSummaryRepository;
import me.Votifier.server.model.documents.DistrictsSummary.DistrictData;
import me.Votifier.server.model.documents.GinglesIncomeDocuments.GinglesIncomeAnalysis;
import me.Votifier.server.model.documents.GinglesRacialDocuments.GinglesRacialAnalysis;
import me.Votifier.server.model.documents.GinglesRacialIncomeDocuments.GinglesRacialIncomeAnalysis;
import me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments.EIRacialAnalysis;
import me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments.EIIncomeAnalysis;
import  me.Votifier.server.model.documents.PlanSplitsDocuments.PlanSplitsAnalysis;

import me.Votifier.server.model.documents.BoxplotRacialDocuments.BoxplotRacialAnalysis;
import me.Votifier.server.model.documents.StateSummary;
import me.Votifier.server.model.exceptions.UnknownFileException;

import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/{stateAbbreviation}/summary")
    public ResponseEntity<Resource> getStateSummary(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherSummaryDataFromCache(stateAbbreviation);
    }
    @GetMapping("/{stateAbbreviation}/districts/summary")
    public ResponseEntity<Resource> getDistrictsSummary(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherDistrictsDataFromCache(stateAbbreviation);
    }
    @GetMapping("/{stateAbbreviation}/gingles/demographics/{racialGroup}")
    public ResponseEntity<Resource> getGinglesAnalysisByRace(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup) {
        return gatherGinglesDataFromCache(stateAbbreviation, racialGroup);
    }
    @GetMapping("/{stateAbbreviation}/gingles/demographics-and-economic/{racialGroup}")
    public ResponseEntity<Resource> getGinglesRacialIncomeAnalysisByRace(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup) {
        return gatherGinglesRacialIncomeDataFromCache(stateAbbreviation, racialGroup);
    }
    @GetMapping("/{stateAbbreviation}/ei-analysis/demographics/{racialGroup}/{regionType}")
    public ResponseEntity<Resource> getRacialEIAnalysisByRegionType(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation,@PathVariable("racialGroup") RacialGroup racialGroup, @PathVariable("regionType") RegionType regionType) {
        return gatherRacialEIAnalysisDataFromCache(stateAbbreviation,racialGroup, regionType);
    }
    @GetMapping("/{stateAbbreviation}/ei-analysis/economics/{economicGroup}/{regionType}")
    public ResponseEntity<Resource> getIncomeEIAnalysisByRegionType(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation,@PathVariable("economicGroup") EconomicGroup economicGroup, @PathVariable("regionType") RegionType regionType) {
        return gatherIncomeEIAnalysisDataFromCache(stateAbbreviation,economicGroup, regionType);
    }
    @GetMapping("/{stateAbbreviation}/boxplot/demographics/{racialGroup}")
    public ResponseEntity<Resource> getBoxplotByRacialGroup(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable("racialGroup") RacialGroup racialGroup) {
        return gatherBoxplotRacialDataFromCache(stateAbbreviation, racialGroup);
    }
    @GetMapping("/{stateAbbreviation}/boxplot/economics/{economicGroup}")
    public ResponseEntity<Resource> getBoxplotByEconomicGroup(
        @PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation,
        @PathVariable("economicGroup") EconomicGroup economicGroup) {
        return gatherBoxplotEconomicDataFromCache(stateAbbreviation, economicGroup);
    }

    @GetMapping("/{stateAbbreviation}/boxplot/region_type/{regionType}")
    public ResponseEntity<Resource> getBoxplotByRegionType(
        @PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation,
        @PathVariable(value = "regionType") RegionType regionType) {
        return gatherBoxplotRegionDataFromCache(stateAbbreviation, regionType);
    }

    @GetMapping("/{stateAbbreviation}/gingles/economic/{regionType}")
    public ResponseEntity<Resource> getGinglesIncomeAnalysisByRegionType(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation, @PathVariable(value = "regionType") RegionType regionType) {
        return gatherGinglesIncomeDataFromCache(stateAbbreviation, regionType);
    }

    @GetMapping("/{stateAbbreviation}/plansplits")
    public ResponseEntity<Resource> getPlanSplits(@PathVariable("stateAbbreviation") StateAbbreviation stateAbbreviation) {
        return gatherPlanSplitsDataFromCache(stateAbbreviation);
    }

    @Autowired
    private StateSummaryRepository stateSummaryRepository;

    @Cacheable(value = "stateSummaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherSummaryDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            StateSummary stateSummary = stateSummaryRepository.findByNAME(stateAbbreviation.getFullStateName());

            if (stateSummary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(stateSummary);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing state summary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private DistrictsSummaryRepository districtsSummaryRepository;

    @Cacheable(value = "districtsSummaries", key = "#stateAbbreviation")
    public ResponseEntity<Resource> gatherDistrictsDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
    
            me.Votifier.server.model.documents.DistrictsSummary districtsSummary = districtsSummaryRepository.findByName(stateName);
    
            if (districtsSummary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            
            String jsonResponse = JSON.toJSONString(districtsSummary, config);
    
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing districts summary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Autowired
    private me.Votifier.server.model.repository.GinglesRacialRepository ginglesRepository;

    @Cacheable(value = "ginglesRacialAnalysis", key = "#stateAbbreviation + '-' + #racialGroup.ginglesIdentifer")
    public ResponseEntity<Resource> gatherGinglesDataFromCache(StateAbbreviation stateAbbreviation, RacialGroup racialGroup) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String racialGroupName = racialGroup.getGinglesIdentifer();
    
            GinglesRacialAnalysis ginglesAnalysis = ginglesRepository.findByNameAndRace(stateName, racialGroupName);
    
            if (ginglesAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            String jsonResponse = JSON.toJSONString(ginglesAnalysis, config);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing gingles analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired
    private me.Votifier.server.model.repository.GinglesRacialIncomeRepository ginglesRacialIncomeRepository;

    @Cacheable(value = "ginglesRacialIncomeAnalysis", key = "#stateAbbreviation + '-' + #racialGroup.ginglesIdentifer")
    public ResponseEntity<Resource> gatherGinglesRacialIncomeDataFromCache(StateAbbreviation stateAbbreviation, RacialGroup racialGroup) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String racialGroupName = racialGroup.getGinglesIdentifer();
    
            GinglesRacialIncomeAnalysis ginglesAnalysis = ginglesRacialIncomeRepository.findByNameAndRace(stateName, racialGroupName);
    
            if (ginglesAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            String jsonResponse = JSON.toJSONString(ginglesAnalysis, config);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing gingles analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.GinglesIncomeRepository ginglesIncomeRepository;

    @Cacheable(value = "ginglesIncomeAnalysis", key = "#stateAbbreviation + '-' + #regionType.type")
    public ResponseEntity<Resource> gatherGinglesIncomeDataFromCache(StateAbbreviation stateAbbreviation, RegionType regionType) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            GinglesIncomeAnalysis ginglesIncomeAnalysis = null;
            
            String regionTypeName = regionType.getType();
            System.out.print("Region Type: " + regionType.getType());
            ginglesIncomeAnalysis = ginglesIncomeRepository.findByNameAndRegionType(stateName, regionTypeName);
            
    
            if (ginglesIncomeAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            String jsonResponse = JSON.toJSONString(ginglesIncomeAnalysis, config);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing gingles income analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.EIRacialRepository eiRacialRepository;

    @Cacheable(value = "eiRacialAnalysis", key = "#stateAbbreviation + '-' + #regionType.type")

    public ResponseEntity<Resource> gatherRacialEIAnalysisDataFromCache(StateAbbreviation stateAbbreviation, RacialGroup racialGroup, RegionType regionType) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            EIRacialAnalysis eiRacialAnalysis = null;
            
            String regionTypeName = regionType.getType();
            System.out.print("Region Type: " + regionType.getType());

            System.out.print("Racial Group: " + racialGroup.getGinglesIdentifer());

            String raceName = racialGroup.getGinglesIdentifer();
            if (raceName.equals("WHITE")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiRacialRepository.findWhiteByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiRacialRepository.findWhiteByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findWhiteByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findWhiteByNameAndUrban(stateName);
                }
            } else if (raceName.equals("BLACK")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiRacialRepository.findBlackByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiRacialRepository.findBlackByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findBlackByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findBlackByNameAndUrban(stateName);
                }
            } else if (raceName.equals("ASIAN")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiRacialRepository.findAsianByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiRacialRepository.findAsianByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findAsianByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findAsianByNameAndUrban(stateName);
                }
            } else if (raceName.equals("HISPANIC")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiRacialRepository.findHispanicByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiRacialRepository.findHispanicByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findHispanicByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiRacialRepository.findHispanicByNameAndUrban(stateName);
                }
            }
    
            if (eiRacialAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            SerializeConfig config = new SerializeConfig();
            
            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });
            String jsonResponse = JSON.toJSONString(eiRacialAnalysis, config);
            Resource resource = new ByteArrayResource(jsonResponse.getBytes());
    
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing ei racial analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.EIIncomeRepository eiIncomeRepository;

    @Cacheable(value = "eiIncomeAnalysis", key = "#stateAbbreviation + '-' + #regionType.type")
    public ResponseEntity<Resource> gatherIncomeEIAnalysisDataFromCache(StateAbbreviation stateAbbreviation, EconomicGroup economicGroup, RegionType regionType) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            EIIncomeAnalysis eiRacialAnalysis = null;
            
            String regionTypeName = regionType.getType();
            System.out.println("Region Type: " + regionType.getType());

            System.out.println("Income Group: " + economicGroup.getGroup());

            System.out.println("State Name: " + stateName);

            String economicGroupName = economicGroup.getGroup();
            if (economicGroupName.equals("0_35K")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome0_35KByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome0_35KByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome0_35KByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome0_35KByNameAndUrban(stateName);
                }
            } else if (economicGroupName.equals("35K_60K")) {
                if (regionTypeName.equals("ALL")) {
                    System.out.println("Finding 35K_60K by All");
                    eiRacialAnalysis = eiIncomeRepository.findIncome35K_60KByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome35K_60KByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome35K_60KByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome35K_60KByNameAndUrban(stateName);
                }
            } else if (economicGroupName.equals("60K_100K")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome60K_100KByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome60K_100KByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome60K_100KByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome60K_100KByNameAndUrban(stateName);
                }
            } else if (economicGroupName.equals("100K_125K")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome100K_125KByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome100K_125KByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome100K_125KByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome100K_125KByNameAndUrban(stateName);
                }
            } else if (economicGroupName.equals("125K_150K")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome125K_150KByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome125K_150KByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome125K_150KByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome125K_150KByNameAndUrban(stateName);
                }
            } else if (economicGroupName.equals("150K_MORE")) {
                if (regionTypeName.equals("ALL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome150K_MOREByNameAndAll(stateName);
                } else if (regionTypeName.equals("RURAL")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome150K_MOREByNameAndRural(stateName);
                } else if (regionTypeName.equals("SUBURBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome150K_MOREByNameAndSuburban(stateName);
                } else if (regionTypeName.equals("URBAN")) {
                    eiRacialAnalysis = eiIncomeRepository.findIncome150K_MOREByNameAndUrban(stateName);
                }
            }
            System.out.println("EI Racial Analysis: " + eiRacialAnalysis);
            if (eiRacialAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(eiRacialAnalysis, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);

        } catch (Exception e) {
            System.out.println("Error fetching or serializing ei income analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.BoxplotRacialRepository boxplotRacialRepository;
    
    @Cacheable(value = "boxplotRacialAnalysis", key = "#stateAbbreviation + '-' + #racialGroup.ginglesIdentifer")

    public ResponseEntity<Resource> gatherBoxplotRacialDataFromCache(StateAbbreviation stateAbbreviation, RacialGroup racialGroup) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String racialGroupName = racialGroup.getGinglesIdentifer();
    
            BoxplotRacialAnalysis boxplotRacialAnalysis = null;

            if (racialGroupName.equals("WHITE")) {
                boxplotRacialAnalysis = boxplotRacialRepository.findWhiteByName(stateName);
            } else if (racialGroupName.equals("BLACK")) {
                boxplotRacialAnalysis = boxplotRacialRepository.findBlackByName(stateName);
            } else if (racialGroupName.equals("ASIAN")) {
                boxplotRacialAnalysis = boxplotRacialRepository.findAsianByName(stateName);
            }

            if (boxplotRacialAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(boxplotRacialAnalysis, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing boxplot racial analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.BoxplotIncomeRepository boxplotIncomeRepository;

    @Cacheable(value = "boxplotIncomeAnalysis", key = "#stateAbbreviation + '-' + #economicGroup.group")

    public ResponseEntity<Resource> gatherBoxplotEconomicDataFromCache(StateAbbreviation stateAbbreviation, EconomicGroup economicGroup) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String economicGroupName = economicGroup.getGroup();
            
            System.out.println("Economic Group: " + economicGroupName);

            me.Votifier.server.model.documents.BoxplotIncomeDocuments.BoxplotIncomeAnalysis boxplotIncomeAnalysis = null;

            if (economicGroupName.equals("0_35K")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome0_35KByName(stateName);
            } else if (economicGroupName.equals("35K_60K")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome35K_60KByName(stateName);
            } else if (economicGroupName.equals("60K_100K")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome60K_100KByName(stateName);
            } else if (economicGroupName.equals("100K_125K")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome100K_125KByName(stateName);
            } else if (economicGroupName.equals("125K_150K")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome125K_150KByName(stateName);
            } else if (economicGroupName.equals("150K_MORE")) {
                boxplotIncomeAnalysis = boxplotIncomeRepository.findIncome150K_MOREByName(stateName);
            }

            if (boxplotIncomeAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(boxplotIncomeAnalysis, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing boxplot income analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.BoxplotRegionRepository boxplotRegionRepository;

    @Cacheable(value = "boxplotRegionAnalysis", key = "#stateAbbreviation + '-' + #regionType.type")

    public ResponseEntity<Resource> gatherBoxplotRegionDataFromCache(StateAbbreviation stateAbbreviation, RegionType regionType) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            String regionTypeName = regionType.getType();
            
            System.out.println("Region Type: " + regionTypeName);

            me.Votifier.server.model.documents.BoxplotRegionDocuments.BoxplotRegionAnalysis boxplotRegionAnalysis = null;

            if (regionTypeName.equals("RURAL")) {
                boxplotRegionAnalysis = boxplotRegionRepository.findRURALByName(stateName);
            } else if (regionTypeName.equals("SUBURBAN")) {
                boxplotRegionAnalysis = boxplotRegionRepository.findSUBURBANByName(stateName);
            } else if (regionTypeName.equals("URBAN")) {
                boxplotRegionAnalysis = boxplotRegionRepository.findURBANByName(stateName);
            }

            if (boxplotRegionAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SerializeConfig config = new SerializeConfig();

            config.addFilter(DistrictData.class, new NameFilter() {
                @Override
                public String process(Object object, String name, Object value) {
                    return name.toUpperCase();
                }
            });

            String jsonResponse = JSON.toJSONString(boxplotRegionAnalysis, config);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing boxplot region analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Autowired me.Votifier.server.model.repository.PlanSplitsRepository planSplitsRepository;

    @Cacheable(value = "planSplits", key = "#stateAbbreviation")

    public ResponseEntity<Resource> gatherPlanSplitsDataFromCache(StateAbbreviation stateAbbreviation) {
        try {
            String stateName = stateAbbreviation.getFullStateName();
            
            PlanSplitsAnalysis planSplitsAnalysis = planSplitsRepository.findByNameAndRace(stateName);

            System.out.println("Plan Splits Analysis: " + planSplitsAnalysis);

            if (planSplitsAnalysis == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }


            String jsonResponse = JSON.toJSONString(planSplitsAnalysis);

            Resource resource = new ByteArrayResource(jsonResponse.getBytes());

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error fetching or serializing plan splits analysis: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Note: This method will eventually be removed, since we will be accessing the cache/database for this data instead of locally
    public Resource getResourceFromLocal(Path filePath) throws UnknownFileException {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            else {
                throw new UnknownFileException();
            }
        }
        catch (MalformedURLException exception) {
            throw new UnknownFileException();
        }
    }
}