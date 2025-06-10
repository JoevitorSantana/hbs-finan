import { useParametros } from "hooks/useParametros";
import React, { useEffect, useState } from "react";
import {Button, Card, Col, Form, Row, Nav, Tab, Modal} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import NavBar from "../../../layouts/AdminLayout/NavBar";
import {GrLinkNext, GrLinkPrevious} from "react-icons/gr";
import {useAuth} from "../../../contexts/AuthProvider";

function formatCNPJ(value) {
    value = value.replace(/\D/g, "");
    return value.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2}).*/, "$1.$2.$3/$4-$5");
}
function formatTelefone(value) {
    value = value.replace(/\D/g, "");
    if (value.length === 10)
        return value.replace(/^(\d{2})(\d{4})(\d{4}).*/, "($1) $2-$3");
    if (value.length === 11)
        return value.replace(/^(\d{2})(\d{5})(\d{4}).*/, "($1) $2-$3");
    return value;
}
function formatCelular(value) {
    value = value.replace(/\D/g, "");
    return value.replace(/^(\d{2})(\d{5})(\d{4}).*/, "($1) $2-$3");
}
function formatCEP(value) {
    value = value.replace(/\D/g, "");
    return value.replace(/^(\d{5})(\d{3}).*/, "$1-$2");
}
function isValidCNPJ(value) {
    value = value.replace(/\D/g, "");
    if (value.length !== 14) return false;
    if (/^(\d)\1{13}$/.test(value)) return false;
    const digits = value.split("").map((d) => +d);
    const calc = (slice, weights) => {
        const sum = slice.reduce((acc, num, idx) => acc + num * weights[idx], 0);
        const r = sum % 11;
        return r < 2 ? 0 : 11 - r;
    };
    const weights1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const weights2 = [6].concat(weights1);
    const d1 = calc(digits.slice(0, 12), weights1);
    const d2 = calc(digits.slice(0, 12).concat(d1), weights2);
    return d1 === digits[12] && d2 === digits[13];
}

const PrimeiroAcessoParametrizacao = () => {
    const { parametros } = useParametros();
    const token = localStorage.getItem("site");
    const usu = localStorage.getItem("user");
    const usuarioLogado = usu ? JSON.parse(usu) : null;
    const navigate = useNavigate();
    const [abaAtiva, setAbaAtiva] = useState('parametrizacao');

    const { logOut } = useAuth();

    const [showModal, setShowModal] = useState(false);

    const [formData, setFormData] = useState({
        id: "",
        nomeEmpresa: "",
        razaoSocial: "",
        enderecoRua: "",
        enderecoBairro: "",
        enderecoCidade: "",
        enderecoCep: "",
        enderecoEstado: "",
        email: "",
        telefone: "",
        celular: "",
        nomeProprietario: "",
        cnpj: "",
        logoPequenaUrl: "",
        logoGrandeUrl: "",
    });
    const [formDataUsuario, setFormDataUsuario] = useState({
        nome: '',
        ultimoNome: '',
        email: '',
        senha: '',
        role: 'ADMIN'
    });

    const requiredFields = [
        "nomeEmpresa", "razaoSocial", "email", "telefone", "celular", "nomeProprietario", "cnpj",
        "enderecoRua", "enderecoBairro", "enderecoCidade", "enderecoEstado", "enderecoCep",
        "logoPequenaUrl", "logoGrandeUrl",
    ];

    const [errors, setErrors] = useState({});
    const examplePlaceholders = {
        telefone: "Exemplo: (00) 0000-0000",
        celular: "Exemplo: (00) 00000-0000",
        cnpj: "Exemplo: 00.000.000/0000-00",
        enderecoCep: "Exemplo: 00000-000",
    };

    const handleShowModal = () => {
        const newErrors = {};

        requiredFields.forEach((field) => {
            if (!formData[field] || formData[field].toString().trim() === "") {
                newErrors[field] = true;
            }
        });

        if (!newErrors.cnpj && !isValidCNPJ(formData.cnpj)) {
            newErrors.cnpj = true;
            toast.error("CNPJ inválido. Verifique os dígitos verificadores.");
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            toast.error("Preencha todos os campos obrigatórios corretamente!");
            return;
        }

        // Validação dos campos
        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    useEffect(() => {
        if (parametros) {
            setFormData({
                id: parametros.id ?? "",
                nomeEmpresa: parametros.nomeEmpresa ?? "",
                razaoSocial: parametros.razaoSocial ?? "",
                enderecoRua: parametros.enderecoRua ?? "",
                enderecoBairro: parametros.enderecoBairro ?? "",
                enderecoCidade: parametros.enderecoCidade ?? "",
                enderecoCep: parametros.enderecoCep ?? "",
                enderecoEstado: parametros.enderecoEstado ?? "",
                email: parametros.email ?? "",
                telefone: parametros.telefone ?? "",
                celular: parametros.celular ?? "",
                nomeProprietario: parametros.nomeProprietario ?? "",
                cnpj: parametros.cnpj ?? "",
                logoPequenaUrl: parametros.logoPequenaUrl ?? "",
                logoGrandeUrl: parametros.logoGrandeUrl ?? "",
            });
            setErrors({});
        }
    }, [parametros]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: false }));
        }
    };

    const handleChangeUsuario = (e) => {
        const { name, value } = e.target;
        setFormDataUsuario(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleFinalizar = async (e) => {
        e.preventDefault();
        const newErrors = {};

        requiredFields.forEach((field) => {
            if (!formData[field] || formData[field].toString().trim() === "") {
                newErrors[field] = true;
            }
        });

        if (!newErrors.cnpj && !isValidCNPJ(formData.cnpj)) {
            newErrors.cnpj = true;
            toast.error("CNPJ inválido. Verifique os dígitos verificadores.");
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            toast.error("Preencha todos os campos obrigatórios corretamente!");
            return;
        }

        // Validação dos campos
        const validationErrors = validateFields();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            toast.error('Por favor, corrija os erros no formulário.');
            return;
        }

        // Formata os campos antes de enviar
        const payload = {
            ...formData,
            cnpj: formatCNPJ(formData.cnpj),
            telefone: formatTelefone(formData.telefone),
            celular: formatCelular(formData.celular),
            enderecoCep: formatCEP(formData.enderecoCep),
        };

        // Inserindo Parametrização
        try {
            const response = await fetch("http://localhost:8080/parametrizacao/novo", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                toast.success("Parametrização atualizada com sucesso!");
                setTimeout(() => navigate("/parametrizacao"), 1800);
            } else if (response.status === 401) {
                toast.error("Sessão expirada. Faça login novamente.");
                localStorage.removeItem("site");
                setTimeout(() => navigate("/login"), 1500);
            } else {
                const text = await response.text();
                toast.error(`Erro (${response.status}): ${text || "desconhecido"}`);
            }
        } catch (err) {
            console.error("Erro ao atualizar parametrização:", err);
            toast.error("Erro na comunicação com o servidor.");
        }

        // Inserindo Usuário
        try {
            const response = await fetch('http://localhost:8080/usuarios/novo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization' : 'Bearer ' + token
                },
                body: JSON.stringify(formDataUsuario)
            });

            if (response.ok) {
                toast.success('Parametrização concluída com sucesso!');
                /* setTimeout(() => {
                    navigate('/usuarios');
                }, 2000); */
            } else {
                const errorData = await response.json();
                toast.error('Erro ao cadastrar usuário: ' + (errorData.message || 'Erro desconhecido'));
            }
        } catch (error) {
            console.error('Erro ao cadastrar usuário:', error);
            toast.error('Erro na comunicação com o servidor.');
        }

        // Excluindo Usuário Admin Padrão
        try {
            const response = await fetch(`http://localhost:8080/usuarios/excluir/${usuarioLogado.id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                },
            });

            if (response.ok) {
                toast.success("Usuário excluído com sucesso!");
                setTimeout(() => {
                    window.location.reload();
                }, 2000);
            } else {
                const errorData = await response.json();
                toast.error(errorData.message || 'Erro ao excluir usuário.');
            }
        } catch (error) {
            console.error('Erro ao excluir usuário:', error);
            toast.error('Erro na comunicação com o servidor.');
        } finally {
            handleCloseModal();
        }

        // desautenticando
        logOut();
    };

    const validateFields = () => {
        const newErrors = {};
        if (!formDataUsuario.nome) newErrors.nome = 'Nome é obrigatório.';
        if (!formDataUsuario.ultimoNome) newErrors.ultimoNome = 'Último nome é obrigatório.';
        if (!formDataUsuario.email) newErrors.email = 'Email é obrigatório.';
        if (!formDataUsuario.senha) newErrors.senha = 'Senha é obrigatória.';
        return newErrors;
    };

    const validarEAlterarAba = (aba) => {
        if (aba == 'usuario'){
            const validationErrors = validateFields();
            if (Object.keys(validationErrors).length > 0) {
                setErrors(validationErrors);
                toast.error('Por favor, corrija os erros no formulário.');
                return;
            }
            setAbaAtiva('parametrizacao');
        }

        if (aba == 'parametrizacao') {
            const newErrors = {};

            requiredFields.forEach((field) => {
                if (!formData[field] || formData[field].toString().trim() === "") {
                    newErrors[field] = true;
                }
            });

            if (!newErrors.cnpj && !isValidCNPJ(formData.cnpj)) {
                newErrors.cnpj = true;
                toast.error("CNPJ inválido. Verifique os dígitos verificadores.");
            }

            if (Object.keys(newErrors).length > 0) {
                setErrors(newErrors);
                toast.error("Preencha todos os campos obrigatórios corretamente!");
                return;
            }
            setAbaAtiva('usuario');
        }
    }

    const column1Fields = [
        { id: "nomeEmpresa", label: "Nome da Empresa", type: "text" },
        { id: "razaoSocial", label: "Razão Social", type: "text" },
        { id: "email", label: "Email", type: "email" },
        { id: "telefone", label: "Telefone", type: "text" },
        { id: "celular", label: "Celular", type: "text" },
        { id: "nomeProprietario", label: "Nome do Proprietário", type: "text" },
        { id: "cnpj", label: "CNPJ", type: "text" },
    ];

    const column2Fields = [
        { id: "enderecoRua", label: "Rua" },
        { id: "enderecoBairro", label: "Bairro" },
        { id: "enderecoCidade", label: "Cidade" },
        { id: "enderecoEstado", label: "Estado" },
        { id: "enderecoCep", label: "CEP" },
        { id: "logoPequenaUrl", label: "Logo Pequena (URL)" },
        { id: "logoGrandeUrl", label: "Logo Grande (URL)" },
    ];

    return (
        <React.Fragment>
            <NavBar />
            <ToastContainer position="top-right" autoClose={2500} />
            <Row>
                <Col>
                    <Tab.Container activeKey={abaAtiva} onSelect={(k) => setAbaAtiva(k)} defaultActiveKey="parametrizacao">
                        <Row style={{display: "flex", justifyContent: "center"}}>
                            <Col sm={3}>
                                <Nav variant="pills" className="flex-column">
                                    <Nav.Item>
                                        <Nav.Link eventKey="parametrizacao">Parametrização</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link eventKey="usuario">Criar Usuário</Nav.Link>
                                    </Nav.Item>
                                </Nav>
                            </Col>
                            <Col sm={7}>
                                <Tab.Content>
                                    <Tab.Pane eventKey="parametrizacao">
                                            <Card.Body>
                                                <Form>
                                                    <Row>
                                                        {/* Coluna 1 */}
                                                        <Col md={6}>
                                                            {column1Fields.map(({ id, label, type }) => (
                                                                <Form.Group className="mb-3" controlId={id} key={id}>
                                                                    <Form.Label>
                                                                        {label} <span className="text-danger">*</span>
                                                                    </Form.Label>
                                                                    <Form.Control
                                                                        type={type}
                                                                        name={id}
                                                                        value={formData[id]}
                                                                        onChange={handleChange}
                                                                        isInvalid={errors[id]}
                                                                        required
                                                                        placeholder={
                                                                            formData[id] === "" && examplePlaceholders[id]
                                                                                ? examplePlaceholders[id]
                                                                                : ""
                                                                        }
                                                                    />
                                                                    <Form.Control.Feedback type="invalid">
                                                                        {id === "cnpj" && errors[id]
                                                                            ? "CNPJ inválido"
                                                                            : "Este campo é obrigatório."}
                                                                    </Form.Control.Feedback>
                                                                </Form.Group>
                                                            ))}
                                                        </Col>

                                                        {/* Coluna 2 */}
                                                        <Col md={6}>
                                                            {column2Fields.map(({ id, label }) => (
                                                                <Form.Group className="mb-3" controlId={id} key={id}>
                                                                    <Form.Label>
                                                                        {label} <span className="text-danger">*</span>
                                                                    </Form.Label>
                                                                    <Form.Control
                                                                        name={id}
                                                                        value={formData[id]}
                                                                        onChange={handleChange}
                                                                        isInvalid={errors[id]}
                                                                        required
                                                                        placeholder={
                                                                            formData[id] === "" && examplePlaceholders[id]
                                                                                ? examplePlaceholders[id]
                                                                                : ""
                                                                        }
                                                                    />
                                                                    <Form.Control.Feedback type="invalid">
                                                                        "Este campo é obrigatório."
                                                                    </Form.Control.Feedback>
                                                                </Form.Group>
                                                            ))}
                                                        </Col>
                                                    </Row>
                                                    <div className="d-flex justify-content-end gap-2">
                                                        <Button variant="primary" onClick={() => validarEAlterarAba('parametrizacao')}>
                                                            Avançar <GrLinkNext />
                                                        </Button>
                                                    </div>
                                                </Form>
                                            </Card.Body>
                                    </Tab.Pane>
                                    <Tab.Pane eventKey="usuario">
                                        <Card.Body>
                                            <Form>
                                                <Row>
                                                    <Col md={6}>
                                                        <Form.Group className="mb-3" controlId="nome">
                                                            <Form.Label>Nome</Form.Label>
                                                            <Form.Control
                                                                type="nome"
                                                                name="nome"
                                                                value={formDataUsuario.nome}
                                                                placeholder="Nome"
                                                                onChange={handleChangeUsuario}
                                                                isInvalid={!!errors.nome}
                                                                isValid={formDataUsuario.nome && !errors.nome}
                                                            />
                                                            <Form.Control.Feedback type="invalid">
                                                                {errors.nome}
                                                            </Form.Control.Feedback>
                                                        </Form.Group>
                                                    </Col>
                                                    <Col md={6}>
                                                        <Form.Group className="mb-3" controlId="ultimoNome">
                                                            <Form.Label>Último Nome</Form.Label>
                                                            <Form.Control
                                                                type="text"
                                                                name="ultimoNome"
                                                                placeholder="Último Nome"
                                                                value={formDataUsuario.ultimoNome}
                                                                onChange={handleChangeUsuario}
                                                                isInvalid={!!errors.ultimoNome}
                                                                isValid={formDataUsuario.ultimoNome && !errors.ultimoNome}
                                                            />
                                                            <Form.Control.Feedback type="invalid">
                                                                {errors.ultimoNome}
                                                            </Form.Control.Feedback>
                                                        </Form.Group>
                                                    </Col>
                                                    <Col md={6}>
                                                        <Form.Group className="mb-3" controlId="email">
                                                            <Form.Label>Email</Form.Label>
                                                            <Form.Control
                                                                type="email"
                                                                name="email"
                                                                placeholder="Email"
                                                                value={formDataUsuario.email}
                                                                onChange={handleChangeUsuario}
                                                                isInvalid={!!errors.email}
                                                                isValid={formDataUsuario.email && !errors.email}
                                                            />
                                                            <Form.Control.Feedback type="invalid">
                                                                {errors.email}
                                                            </Form.Control.Feedback>
                                                        </Form.Group>
                                                    </Col>
                                                    <Col md={6}>
                                                        <Form.Group className="mb-3" controlId="senha">
                                                            <Form.Label>Senha</Form.Label>
                                                            <Form.Control
                                                                type="password"
                                                                name="senha"
                                                                placeholder="Senha"
                                                                value={formDataUsuario.senha}
                                                                onChange={handleChangeUsuario}
                                                                isInvalid={!!errors.senha}
                                                                isValid={formDataUsuario.senha && !errors.senha}
                                                            />
                                                            <Form.Control.Feedback type="invalid">
                                                                {errors.senha}
                                                            </Form.Control.Feedback>
                                                        </Form.Group>
                                                    </Col>
                                                </Row>
                                                <div className="d-flex justify-content-end gap-2">
                                                    <Button variant="secondary" onClick={() => validarEAlterarAba('usuario')}>
                                                        <GrLinkPrevious /> Anterior
                                                    </Button>
                                                    <Button variant="success" onClick={() => handleShowModal()}>
                                                        Salvar
                                                    </Button>
                                                </div>
                                            </Form>
                                        </Card.Body>
                                    </Tab.Pane>
                                </Tab.Content>
                            </Col>
                        </Row>
                    </Tab.Container>
                </Col>
            </Row>
            {/* Modal de Confirmação */}
            <ModalPrimeiroAcessoConfirmacao
                show={showModal}
                onHide={handleCloseModal}
                onConfirm={handleFinalizar}
            />
        </React.Fragment>
    );
};

export default PrimeiroAcessoParametrizacao;


const ModalPrimeiroAcessoConfirmacao = ({ show, onHide, onConfirm }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>Confirmar Alterações</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                Você será deslogado, e o usuário de primeiro acesso admin@admin será excluído!
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>
                    Cancelar
                </Button>
                <Button variant="success" onClick={onConfirm}>
                    Confirmar
                </Button>
            </Modal.Footer>
        </Modal>
    );
};