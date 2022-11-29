package j4rent.Locatarios.PreContrato.evalute;

/**
 *
 * @author Samic
 */
public class DataBaseFields {
    public static Object[][] DatabaseVariables(int value) {
        Object[][] _prop = {
            {"rgprp","codigo","Código"},
            {"tipoprop","tipo","Tipo 'Fisica' ou 'Jurídica'"},
            {"sexo","sexo","Sexo"},
            {"nome","nome","nome"},
            {"end","endereco", "Endereço"},
            {"num","numero","Número"},
            {"compl","complemento","Complemento"},
            {"bairro","bairro","Bairro"},
            {"cidade","cidade","Cidade"},
            {"estado","estado","Estado"},
            {"cep","cep","Cep"},
            {"tel","telefone","Telefone"},
            {"dtnasc","nascimento","Data de nascimento"},
            {"cpfcnpj","cpf_cnpj","Cpf ou Cnpj"},
            {"rginsc","rg_insc","RG ou Inscrição"},
            {"ecivil","estado_civil","Estado civil"},
            {"naciona","nacionalidade","Nacionalidade"},
            {"profissao","profissao","Profissão"},
            {"representante","represenrante","Representantre"},
            {"repdtnasc","repnasc","Data nascimento representante"},
            {"conjugue","conjugue","Conjugue"},
            {"conjdtnasc","dtnascconj","Data nascimento conjugue"},
            {"email","email","E-Mail"}            
        };
        Object[][] _imov = {
            {"rgimv","registro","Registro"},
            {"tpimovel","tipo","Tipo do Imóvel"},
            {"end","endereco","Endereço"},
            {"num","numero","Número"},
            {"compl","complemento","Complemento"},
            {"bairro","bairro","Bairro"},
            {"cidade","cidade","Cidade"},
            {"estado","estado","Estado"},
            {"cep","cep","Cep"},
            {"situacao","situacao","Situação"}
        };
        Object[][] _loca = {
            {"contrato","contrato","Contrato"},
            {"tploca","tipo","Tipo 'Física' ou 'Jurídica'"},
            {"cpfcnpj","cpf_cnpj","CPF ou CNPJ"},
            {"rginsc","rg_insc","RG ou Inscrição"},
            {"sexo","sexo","Sexo quando Física"},
            {"nomerazao","nome_razao","Nome ou Razão social"},
            {"fantasia","fantasia","Nome fantasia quando jurídica"},
            {"end","endereco","Endereço"},
            {"num","numero","Número"},
            {"compl","complemento","Complemento"},
            {"bairro","bairro","Bairro"},
            {"cidade","cidade","Cidade"},
            {"estado","estado","Estado"},
            {"cep","cep","Cep"},
            {"ecivil","ecivil","Estado civil"},
            {"naciona","nacionalidade","Nacionalidade"},
            {"tel","telefone","Telefone"},
            {"mae","mae","Mãe"},
            {"pai","pai","Pai"},
            {"email","email","E-Mail"},
            {"empresa","empresa","Empresa"},
            {"cargo","profissao","Profissão"},
            {"salario","salario","Salário"},
            {"conjugue","conjugue","Conjugue"},
            {"cjdtnasc", "dtnascconj","Data nascimento conjugue"}
        };
        Object[][] _soci = {
            {"socionome1","nome_1","Nome sócio 1"},
            {"sociodtnasc1","nascimento_1","Data nascimento sócio 1"},
            {"socioecivil1","ecivil_1","Estado civil sócio 1"},
            {"sociocpf1","cpf_1","Cpf do sócio 1"},
            {"sociorg1","rg_1","RG do sócio 1"},
            {"sociomae1","mae_1","Mãe do sócio 1"},
            {"sociopai1","pai_1","Pai do sócio 1"},
            {"sociocargo1","cargo_1","Cargo do sócio 1"},
            {"sociosalario1","salario_1","Salário do sócio 1"},

            {"socionome2","nome_2","Nome sócio 2"},
            {"sociodtnasc2","nascimento_2","Data nascimento sócio 2"},
            {"socioecivil2","ecivil_2","Estado civil sócio 2"},
            {"sociocpf2","cpf_2","Cpf do sócio 2"},
            {"sociorg2","rg_2","RG do sócio 2"},
            {"sociomae2","mae_2","Mãe do sócio 2"},
            {"sociopai2","pai_2","Pai do sócio 2"},
            {"sociocargo2","cargo_2","Cargo do sócio 2"},
            {"sociosalario2","salario_2","Salário do sócio 2"},
            
            {"socionome3","nome_3","Nome sócio 3"},
            {"sociodtnasc3","nascimento_3","Data nascimento sócio 3"},
            {"socioecivil3","ecivil_3","Estado civil sócio 3"},
            {"sociocpf3","cpf_3","Cpf do sócio 3"},
            {"sociorg3","rg_3","RG do sócio 3"},
            {"sociomae3","mae_3","Mãe do sócio 3"},
            {"sociopai3","pai_3","Pai do sócio 3"},
            {"sociocargo3","cargo_3","Cargo do sócio 3"},
            {"sociosalario3","salario_3","Salário do sócio 3"},
            
            {"socionome4","nome_4","Nome sócio 4"},
            {"sociodtnasc4","nascimento_4","Data nascimento sócio 4"},
            {"socioecivil4","ecivil_4","Estado civil sócio 4"},
            {"sociocpf4","cpf_4","Cpf do sócio 4"},
            {"sociorg4","rg_4","RG do sócio 4"},
            {"sociomae4","mae_4","Mãe do sócio 4"},
            {"sociopai4","pai_4","Pai do sócio 4"},
            {"sociocargo4","cargo_4","Cargo do sócio 4"},
            {"sociosalario4","salario_4","Salário do sócio 4"}            
        };
        Object[][] _fiad = {
            {"tploca","tipo_1","Tipo 'Física' ou 'Jurídica'"},
            {"cpfcnpj","cpf_cnpj_1","Cpf ou Cnpj"},
            {"rginsc","rg_insc_1","Rg ou Inscrição"},
            {"sexo","sexo_1","Sexo quando Física"},
            {"nomerazao","nome_razao_1","Nome ou Razão social"},
            {"fantasia","fantasia_1","Nome fantasia quando jurídica"},
            {"end","endereco_1","Endereço"},
            {"num","numero_1","Número"},
            {"compl","complemento_1","Complemento"},
            {"bairro","bairro_1","Bairro"},
            {"cidade","cidade_1","Cidade"},
            {"estado","estado_1","Estado"},
            {"cep","cep_1","Cep"},
            {"ecivil","ecivil_1","Estado civil"},
            {"naciona","nacionalidade_1","Nacionalidade"},
            {"tel","telefone_1","Telefone"},
            {"mae","mae_1","Mãe"},
            {"pai","pai_1","Pai"},
            {"email","email_1","E-Mail"},
            {"empresa","empresa_1","Empresa"},
            {"cargo","profissao_1","Profissão"},
            {"salario","salario_1","Salário"},
            {"conjugue","conjugue_1","Conjugue"},
            {"cjdtnasc", "dtnascconj_1","Data nascimento conjugue"},

            {"tploca","tipo_2","Tipo 'Física' ou 'Jurídica'"},
            {"cpfcnpj","cpf_cnpj_2","Cpf ou Cnpj"},
            {"rginsc","rg_insc_2","Rg ou Inscrição"},
            {"sexo","sexo_2","Sexo quando Física"},
            {"nomerazao","nome_razao_2","Nome ou Razão social"},
            {"fantasia","fantasia_2","Nome fantasia quando jurídica"},
            {"end","endereco_2","Endereço"},
            {"num","numero_2","Número"},
            {"compl","complemento_2","Complemento"},
            {"bairro","bairro_2","Bairro"},
            {"cidade","cidade_2","Cidade"},
            {"estado","estado_2","Estado"},
            {"cep","cep_2","Cep"},
            {"ecivil","ecivil_2","Estado civil"},
            {"naciona","nacionalidade_2","Nacionalidade"},
            {"tel","telefone_2","Telefone"},
            {"mae","mae_2","Mãe"},
            {"pai","pai_2","Pai"},
            {"email","email_2","E-Mail"},
            {"empresa","empresa_2","Empresa"},
            {"cargo","profissao_2","Profissão"},
            {"salario","salario_2","Salário"},
            {"conjugue","conjugue_2","Conjugue"},
            {"cjdtnasc", "dtnascconj_2","Data nascimento conjugue"},

            {"tploca","tipo_2","Tipo 'Física' ou 'Jurídica'"},
            {"cpfcnpj","cpf_cnpj_2","Cpf ou Cnpj"},
            {"rginsc","rg_insc_2","Rg ou Inscrição"},
            {"sexo","sexo_2","Sexo quando Física"},
            {"nomerazao","nome_razao_2","Nome ou Razão social"},
            {"fantasia","fantasia_2","Nome fantasia quando jurídica"},
            {"end","endereco_2","Endereço"},
            {"num","numero_2","Número"},
            {"compl","complemento_2","Complemento"},
            {"bairro","bairro_2","Bairro"},
            {"cidade","cidade_2","Cidade"},
            {"estado","estado_2","Estado"},
            {"cep","cep_2","Cep"},
            {"ecivil","ecivil_2","Estado civil"},
            {"naciona","nacionalidade_2","Nacionalidade"},
            {"tel","telefone_2","Telefone"},
            {"mae","mae_2","Mãe"},
            {"pai","pai_2","Pai"},
            {"email","email_2","E-Mail"},
            {"empresa","empresa_2","Empresa"},
            {"cargo","profissao_2","Profissão"},
            {"salario","salario_2","Salário"},
            {"conjugue","conjugue_2","Conjugue"},
            {"cjdtnasc", "dtnascconj_2","Data nascimento conjugue"}

        };
        Object[][] _cart = {
            {"DTINICIO","dtinicio","Data de inicio de Contrato"},
            {"DTTERMINO","dttermino","Data de fim de Contrato"},
            {"DTADITO","dtadito","Data de aditamento de Contrato"},
            {"DTVENCIMENTO","vencimento","Data de vencimento"},
            {"01","aluguel","Valor do Aluguel"},
            {"02","condominio","Valor do Condomínio"},
            {"07","iptu","Valor do IPTU"}            
        };
        Object[][] retorno = null;
        switch (value) {
            case 0:
                retorno = _prop;
                break;
            case 1:
                retorno = _imov;
                break;
            case 2:
                retorno = _loca;
                break;
            case 3:
                retorno = _soci;
                break;
            case 4:
                retorno = _fiad;
                break;
            case 5:
                retorno = _cart;
                break;
            default:
                retorno = null;
        }
        return retorno;
    }     
}
