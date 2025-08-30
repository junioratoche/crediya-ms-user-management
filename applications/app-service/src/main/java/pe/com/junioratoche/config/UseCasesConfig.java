package pe.com.junioratoche.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import pe.com.junioratoche.model.user.User;
import pe.com.junioratoche.model.user.validation.ReactiveValidator;
import pe.com.junioratoche.model.user.validation.UserAggregatingValidator;
import pe.com.junioratoche.model.user.validation.ports.EmailUniquenessPort;
import pe.com.junioratoche.model.user.validation.provider.RuleProvider;
import pe.com.junioratoche.model.user.validation.provider.UserCreateRuleProvider;
import pe.com.junioratoche.r2dbc.UserReactiveRepository;

@Configuration
@ComponentScan(basePackages = {"pe.com.junioratoche.usecase", "pe.com.junioratoche.r2dbc"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = true)
public class UseCasesConfig {
        @Bean
        public UserAggregatingValidator userAggregatingValidator() {
                return new UserAggregatingValidator();
        }

        @Bean
        public RuleProvider<User> userCreateRuleProvider(EmailUniquenessPort emailPort) {
                return new UserCreateRuleProvider(emailPort);
        }

        @Bean
        public ReactiveValidator userCreateValidator(RuleProvider<User> provider) {
                return new ReactiveValidator(provider.rulesForCreate());
        }
}
