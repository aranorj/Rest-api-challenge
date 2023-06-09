package com.pinapp.challenge.controller;

import com.pinapp.challenge.datatransfer.BadReqExceptionResponse;
import com.pinapp.challenge.datatransfer.ClientDTO;
import com.pinapp.challenge.datatransfer.ClientDTORes;
import com.pinapp.challenge.datatransfer.KpiDTO;
import com.pinapp.challenge.exception.AgeConflictException;
import com.pinapp.challenge.persistence.entities.Client;
import com.pinapp.challenge.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping("creacliente")
    @Operation(summary = "Crear cliente", description = "Ingresa un nuevo cliente a la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BadReqExceptionResponse.class)))
    })

    public ResponseEntity<Client> crearCliente(@Valid @RequestBody ClientDTO clientDTO) throws AgeConflictException, ParseException {
        Client newClient = clientService.createClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @GetMapping("kpideclientes")
    @Operation(summary = "KPI de Clientes",
            description = "Devuelve el promedio de edad y la desviación estándar entre las edades de todos los clientes")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    public ResponseEntity<KpiDTO> kpiClientes(){
        try{
            KpiDTO kpi = clientService.getKpiAllClientes();
            return ResponseEntity.ok().body(kpi);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("listclientes")
    @Operation(summary = "Listar clientes",
            description = "Devuelve los datos de todos los clientes con su fecha probable de muerte de cada uno," +
                    " basada en datos de mortandad de una población hipotética.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<ClientDTORes>> listClients(){
        try{
            List<ClientDTORes> allClients = clientService.getAllClients();
            return ResponseEntity.ok().body(allClients);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
