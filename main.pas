unit main;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, FileUtil, Forms, Controls, Graphics, Dialogs, Menus,
  ComCtrls, StdCtrls, ExtCtrls;

type

  { TfrmMain }

  TfrmMain = class(TForm)
    btSend: TButton;
    chkCRLF: TCheckBox;
    mnuPortCustom: TMenuItem;
    mnuHardwareFlowCtrl: TMenuItem;
    mnuStopBits: TMenuItem;
    mnuDataBits: TMenuItem;
    mnuParity: TMenuItem;
    mnuPort: TMenuItem;
    mnuBaudRate: TMenuItem;
    mnuExit: TMenuItem;
    mnuSetup: TMenuItem;
    panSendCommand: TPanel;
    txtSend: TEdit;
    mmoConsole: TMemo;
    mnuHelp: TMenuItem;
    mnuEdit: TMenuItem;
    mnuFile: TMenuItem;
    mnuMain: TMainMenu;
    panSend: TPanel;
    stbMain: TStatusBar;
  private
    { private declarations }
  public
    { public declarations }
  end;

var
  frmMain: TfrmMain;

implementation

{$R *.lfm}

end.

