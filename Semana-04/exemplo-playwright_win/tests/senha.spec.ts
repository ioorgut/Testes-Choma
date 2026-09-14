import { test, expect } from '@playwright/test';

const casos = [
  {
    senha: 'Senha12',
    confirmacao: 'Senha12',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'abaixo do limite mínimo'
  },
  {
    senha: 'Senha123',
    confirmacao: 'Senha123',
    resultado: 'Senha cadastrada',
    valido: true,
    classe: 'limite mínimo de 8 caracteres'
  },
  {
    senha: 'Senha1234',
    confirmacao: 'Senha1234',
    resultado: 'Senha cadastrada',
    valido: true,
    classe: 'acima do limite mínimo'
  },
  {
    senha: 'SenhaValida123456789',
    confirmacao: 'SenhaValida123456789',
    resultado: 'Senha cadastrada',
    valido: true,
    classe: 'limite máximo'
  },
  {
    senha: 'SenhaValida1234567890',
    confirmacao: 'SenhaValida1234567890',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'acima do limite máximo'
  },
  {
    senha: 'senha123',
    confirmacao: 'senha123',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'sem letra maiúscula'
  },
  {
    senha: 'SENHA123',
    confirmacao: 'SENHA123',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'sem letra minúscula'
  },
  {
    senha: 'SenhaTeste',
    confirmacao: 'SenhaTeste',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'sem número'
  },
  {
    senha: 'Senha 123',
    confirmacao: 'Senha 123',
    resultado: 'Senha fora do padrão',
    valido: false,
    classe: 'com espaço'
  },
  {
    senha: 'Senha123',
    confirmacao: 'Senha456',
    resultado: 'As senhas não coincidem',
    valido: false,
    classe: 'senhas diferentes'
  }
];

for (const caso of casos) {

  test(`senha - ${caso.classe}`, async ({ page }) => {

    await page.goto('/senha');

    await page
      .getByLabel('Nova senha')
      .fill(caso.senha);

    await page
      .getByLabel('Confirmar senha')
      .fill(caso.confirmacao);

    await page
      .getByRole('button', { name: 'Cadastrar senha' })
      .click();

    const resultado = page.locator('#resultado');

    await expect(resultado).toBeVisible();

    await expect(resultado).toHaveText(caso.resultado);

    await expect(resultado).toHaveAttribute(
      'role',
      caso.valido ? 'status' : 'alert'
    );

  });

}