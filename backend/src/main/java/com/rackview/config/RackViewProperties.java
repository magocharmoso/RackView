package com.rackview.config;

import com.rackview.domain.Bastidor;
import com.rackview.domain.DataCenter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "rackview")
public class RackViewProperties {

    private List<DataCenterConfig> dataCenters = new ArrayList<>();

    public List<DataCenter> toDomain() {
        return dataCenters.stream()
                .map(this::toDataCenter)
                .collect(Collectors.toList());
    }

    private DataCenter toDataCenter(DataCenterConfig dc) {
        List<Bastidor> bastidores = dc.getBastidores() == null ? List.of() : dc.getBastidores().stream()
                .map(b -> new Bastidor(b.getNome(), b.getNickname(), List.of()))
                .collect(Collectors.toList());

        return new DataCenter(dc.getNome(), bastidores);
    }

    public List<DataCenterConfig> getDataCenters() {
        return dataCenters;
    }

    public void setDataCenters(List<DataCenterConfig> dataCenters) {
        this.dataCenters = dataCenters;
    }

    public static class DataCenterConfig {
        private String nome;
        private List<BastidorConfig> bastidores = new ArrayList<>();

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public List<BastidorConfig> getBastidores() {
            return bastidores;
        }

        public void setBastidores(List<BastidorConfig> bastidores) {
            this.bastidores = bastidores;
        }
    }

    public static class BastidorConfig {
        private String nome;
        private String nickname;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
