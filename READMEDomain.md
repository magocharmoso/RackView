# Domain Rules — RackView

## 1. Modelo de domínio

A estrutura do domínio deve seguir esta ideia:

- `Equipamento` é uma interface pública
- as implementações concretas são: `Switch`, `Router`, `SateliteRouter`, `Servidor`, `Firewall`
- o nome do equipamento fica na interface como contrato mínimo
- a lógica comum pode viver numa classe abstrata interna/package-private, mas a API pública continua a ser a interface

Recomendação prática:

- `Equipamento` define apenas o contrato essencial
- `AbstractEquipamento` guarda a implementação comum de `name` e `colour`
- as classes concretas apenas implementam o comportamento específico de cada tipo

### Interface pública recomendada

```java
public interface Equipamento {
    String getName();
    void setName(String name);
    String getColour();
}
```

Esta abordagem mantém a API simples e clara. O utilizador usa sempre a interface, mas as implementações concretas têm regras específicas.

## 2. Padrão recomendado

Para evitar duplicação de código, o melhor padrão aqui é:

- interface: `Equipamento`
- classe abstrata: `AbstractEquipamento`
- classes concretas: `Switch`, `Router`, `SateliteRouter`, `Servidor`, `Firewall`

### Porquê não usar só interface?

Porque, em Java, uma interface não pode ter campos concretos nem lógica de base compartilhada. Se o contrato for apenas `name` + `getColour`, a interface fica muito simples, mas a implementação comum precisa de uma base abstrata para não repetir código.

Em resumo:

- interface = contrato público
- classe abstrata = implementação comum
- classes concretas = regras de negócio específicas

## 3. Regras de nomenclatura

### 3.1 Switch

- o nome deve começar por `SW`
- exemplo válido: `SW-01`, `SW-Core`, `SW-Access`
- o sistema deve validar o prefixo antes de aceitar o equipamento

### 3.2 Router

- o nome deve começar por `GT`
- exemplo válido: `GT-01`, `GT-Core`, `GT-Edge`

### 3.3 Firewall

- regra que se pediu: deve começar por `SW`
- exemplo válido: `SW-FW-01`
- atenção: se mais tarde quiserem distinguir claramente switches e firewalls, a regra recomendada passa a ser `FW`, mas neste domínio seguimos a regra pedida: `SW` também para firewalls

### 3.4 Satelite de Router

- o nome deve começar por `Sat`
- o formato esperado é `SatXYZ`, onde:
  - `X` = número sequencial
  - `YZ` = identificador do router ao qual o satélite está ligado
- o primeiro satélite de um router deve ser gerado como:
  - `Sat1GT01`
  - ou, se o router for `GT-01`, então a convenção normalizada pode ser `Sat1GT-01`
- a regra prática mais estável é:
  - remover caracteres de separação (`-`, `_`, espaço)
  - manter: `Sat1GT01`

### 3.5 Regra do satélite

O `SateliteRouter` deve ter um `Router pai` obrigatório.

- o construtor deve receber apenas o router pai
- o sistema gera automaticamente o nome do satélite
- o satélite deve estar no mesmo `Bastidor` do router pai
- por enquanto, esta regra é validada no próprio domínio e no serviço que cria o satélite

Exemplo:

```java
Router pai = new Router("GT-01");
SateliteRouter sat = new SateliteRouter(pai);
// sat.getName() -> "Sat1GT01"
```

## 4. Regra de criação automática do satélite

O construtor do satélite deve funcionar assim:

- recebe apenas o `Router` pai
- lê o nome do router pai
- calcula o próximo índice sequencial para esse router
- gera o nome do satélite
- valida que o satélite e o pai pertencem ao mesmo `Bastidor`

Pseudocódigo:

```java
public SateliteRouter(Router pai) {
    this.pai = Objects.requireNonNull(pai);
    this.name = gerarNomeAutomatico(pai);
    validarMesmoBastidor(pai, this);
}
```

## 5. Cor (colour)

A cor é um getter obrigatório da interface:

```java
String getColour();
```

A implementação pode ser:

- fixa por tipo (`Switch` = azul, `Router` = verde, etc.)
- ou determinada por uma tabela de cores
- ou calculada por regra de negócio

O importante é que a cor seja uma característica do equipamento e não da infraestrutura.

## 6. Integridade do domínio

As regras de negócio devem estar no domínio, não na camada REST.

Isso significa que:

- `SateliteRouter` valida o pai
- `Router` e `Bastidor` controlam o contexto em que o equipamento vive
- `Equipamento` apenas garante o contrato mínimo

## 7. Recomendação final

A abordagem correta para este projeto é:

- manter `Equipamento` como interface
- usar `AbstractEquipamento` para evitar repetição
- usar classes concretas por tipo
- validar nome, cor e relacionamento do satélite no próprio domínio

Assim o sistema fica simples, extensível e coerente com a regra de negócio que pediste.
