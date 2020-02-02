package br.com.alura.forum.config.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErroValidacaoHandler {
     
    @Autowired
	private MessageSource messageSource;
	
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException e){
	  List<ErroDeFormularioDto> errorsDto = new ArrayList<>();
	  List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
	  
	  fieldErrors.forEach(x -> {
      String mensagem = messageSource.getMessage(x, LocaleContextHolder.getLocale());
	  ErroDeFormularioDto erro = new ErroDeFormularioDto(x.getField(), mensagem);
	  errorsDto.add(erro);
	  });
	  return errorsDto;
	}
}
