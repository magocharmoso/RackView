# Domain Rules — RackView

## 1. Objetivo do domínio

O domínio da aplicação representa a infraestrutura de rede:

- `DataCenter` contém bastidores
- `Bastidor` contém equipamentos
- `Equipamento` é a abstração do que o utilizador regista no bastidor
- os bastidores e data centers são fixos e configurados pela app
- o utilizador apenas regista equipamentos dentro dos bastidores existentes

## 2. Regra geral de sanitização

Todos os inputs de equipamento passam por uma sanitização antes de serem validados.

### Regras de sanitização:

- remover espaços em branco
- remover tabs, múltiplos espaços e quebra de linha
- rejeitar input vazio
- converter para maiúsculas todos os equipamentos que não sejam servidores
- manter servidores como default caso o nome não encaixe em nenhuma regra específica

Exemplo:

- `gt 01` → `GT01`
- `sw core` → `SWCORE`
- `fw lan` → `FWLAN`
- `oobm core` → `OOBMCORE`
- `server-db` → `server-db`

## 3. Regras de nome dos equipamentos

### 3.1 Switch

- deve começar por `SW`
- exemplos válidos: `SW01`, `SW-CORE`, `SWCORE`
- o sistema converte para maiúsculas

### 3.2 Router

- deve começar por `GT`
- exemplos válidos: `GT01`, `GT6`, `GT-EDGE`
- o sistema converte para maiúsculas

### 3.3 Firewall

- deve começar por `FW`
- exemplos válidos: `FW01`, `FW-CORE`, `FWLAN`
- o sistema converte para maiúsculas

### 3.4 OOBM

- deve começar ou terminar com `OOBM`
- exemplos válidos: `OOBM-CORE`, `CORE-OOBM`, `OOBMCORE`
- o sistema converte para maiúsculas

### 3.5 Servidor

- é a opção padrão quando nada encaixa nas regras anteriores
- não existe prefixo obrigatório
- não é convertido para maiúsculas
- exemplos válidos: `server-db`, `db-prod`, `app01`

### 3.6 Satélite

- deve começar por `Sat`
- formato aceitável: `SatXYZ` ou `SatXY`
- exemplos válidos:
  - `Sat1GT6`
  - `Sat2GT9`
  - `Sat1GT12`
  - `Sat3GT3`

Regra importante:

- `Sat` significa “satélite de router”
- o nome do satélite deriva do router pai
- se o router for `GT6`, o satélite pode ser `Sat1GT6`
- se o router for `GT12`, o satélite pode ser `Sat1GT12`

## 4. Regra do satélite

O satélite possui obrigatoriamente um `Router` pai.

- o construtor recebe o router pai
- o nome é gerado automaticamente
- a validação assegura que o formato é `SatXYZ` ou `SatXY`
- por enquanto, o satélite deve pertencer ao mesmo bastidor do router pai

## 5. Fábrica de equipamentos

Para simplificar o uso do utilizador, criámos uma `EquipamentoFactory`.

A ideia é esta:

- o utilizador chama uma única operação de registo
- o sistema normaliza o input
- a fábrica decide o tipo do equipamento
- o sistema cria a implementação correta

Exemplo:

```java
Equipamento e1 = EquipamentoFactory.criar("gt-01", null);
Equipamento e2 = EquipamentoFactory.criar("sw-core", null);
Equipamento e3 = EquipamentoFactory.criar("fw-lan", null);
Equipamento e4 = EquipamentoFactory.criar("oobm-core", null);
Equipamento e5 = EquipamentoFactory.criar("server-db", null);
```

A fábrica deve seguir esta ordem:

1. satélite
2. router
3. switch
4. firewall
5. OOBM
6. servidor (default)

## 6. Praticidade da abordagem

A utilização de uma única operação para registar o equipamento é prática porque:

- reduz a fricção para o utilizador
- centraliza as regras de negócio
- evita que cada endpoint tenha lógica diferente
- torna a validação consistente em todo o domínio

O trade-off é que a fábrica precisa de seguir regras muito claras para inferir o tipo corretamente. Se o input não respeitar a convenção, o sistema rejeita.

## 7. Arquitetura recomendada

A estrutura recomendada é:

- interface: `Equipamento`
- classe abstrata: `AbstractEquipamento`
- classes concretas: `Switch`, `Router`, `Firewall`, `OOBM`, `Servidor`, `SateliteRouter`
- fábrica: `EquipamentoFactory`

Isto oferece:

- domínio limpo
- validação no local certo
- regras repetíveis
- pouca complexidade para o utilizador

## 8. Resumo da regra final

- sem espaços no nome
- nomes de equipamentos (exceto servidores) em maiúsculas
- firewalls começam em `FW`
- switches começam em `SW`
- routers começam em `GT`
- OOBM começa ou termina com `OOBM`
- satélites começam em `Sat`
- servidor é a opção default quando nada encaixa nas regras
- satélite gera nome automaticamente pelo router pai
- utilização de uma fábrica para inferir o tipo do equipamento
