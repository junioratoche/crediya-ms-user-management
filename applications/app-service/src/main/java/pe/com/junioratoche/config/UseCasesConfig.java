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
@ComponentScan(basePackages = "pe.com.junioratoche.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
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
        public ReactiveValidator<User> userCreateValidator(RuleProvider<User> provider) {
                int cpu = Math.min(Runtime.getRuntime().availableProcessors(), 8);
                return new ReactiveValidator<>(provider::rulesForCreate, cpu);
        }
}
