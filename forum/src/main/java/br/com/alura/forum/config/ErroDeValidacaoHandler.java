package br.com.alura.forum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    //Essa classe é utilizada para retornar uma mensagem no idioma do cliente
    @Autowired
    private MessageSource messageSource;


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDTO> handler(MethodArgumentNotValidException exception){

        List<ErroDeFormularioDTO> dto = new ArrayList<ErroDeFormularioDTO>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e->{
            //Adicionando internacionalização no retorno da mensagem
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroDeFormularioDTO  erro = new ErroDeFormularioDTO(e.getField(), message);

            dto.add(erro);
        });

        return dto;
    }
}
