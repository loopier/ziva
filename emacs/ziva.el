;;; ziva.el --- Minor mode to code  t; -*-

;; Copyright (C) 2024  Roger Pibernat

;; Author: Roger Pibernat <alo@rogerpibernat.com>
;; Keywords:

;; This program is free software; you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <https://www.gnu.org/licenses/>.

;;; Commentary:

;;

;;; Code:

(define-minor-mode ziva-mode
  "Toggles local ziva-mode."
  nil   ; Initial value, disabled
  :global nil
  :group 'ziva
  :lighter "ziva"
  ;; :keymap

  (if ziva-mode
      (message "ziva-mode activated")
    (message "ziva-mode deactivated")))

(add-hook 'ziva-mode-hook (lambda () (message "Ziva hook executed")))
(add-hook 'ziva-mode-on-hook (lambda () (message "Ziva turned on")))
(add-hook 'ziva-mode-off-hook (lambda () (message "Ziva turned off")))

(setq ziva-mixer-next-available-track 0)

;; (map! :leader :n "o s" 'sclang-start :desc "Open Supercollider") ;; global under 'SPC-o' menu
;; (map! (:map ziva-mode-map :localleader :n "m" 'sclang-start))
;; (map! :map sclang-mode-map :localleader :n "z" 'ziva-livecoder)
(map! :map ziva-mode-map :localleader :n "a" 'ziva-agent)
(map! :map ziva-mode-map :localleader :n "i" 'ziva-insert)

(map! (:map sclang-mode-map :localleader :n "z b" 'ziva-boot))
;; track
(map! (:map sclang-mode-map :localleader :n "t i" 'ziva-insert-track))
(map! (:map sclang-mode-map :localleader :n "t 1" 'ziva-insert-track-1))
(map! (:map sclang-mode-map :localleader :n "t 2" 'ziva-insert-track-2))
(map! (:map sclang-mode-map :localleader :n "t 3" 'ziva-insert-track-3))
(map! (:map sclang-mode-map :localleader :n "t 4" 'ziva-insert-track-4))
(map! (:map sclang-mode-map :localleader :n "t 5" 'ziva-insert-track-5))
(map! (:map sclang-mode-map :localleader :n "t 6" 'ziva-insert-track-6))
(map! (:map sclang-mode-map :localleader :n "t 7" 'ziva-insert-track-7))
(map! (:map sclang-mode-map :localleader :n "t 8" 'ziva-insert-track-8))
(map! (:map sclang-mode-map :localleader :n "t 9" 'ziva-insert-track-9))
(map! (:map sclang-mode-map :localleader :n "t t" 'ziva-goto-track))
;; track parameters
(map! (:map sclang-mode-map :localleader :n "t I" 'ziva-goto-track-instrument))
(map! (:map sclang-mode-map :localleader :n "t u" 'ziva-goto-track-dur))
(map! (:map sclang-mode-map :localleader :n "t d" 'ziva-goto-track-degree))
(map! (:map sclang-mode-map :localleader :n "t o" 'ziva-goto-track-octave))
(map! (:map sclang-mode-map :localleader :n "t a" 'ziva-goto-track-amp))
(map! (:map sclang-mode-map :localleader :n "t f" 'ziva-goto-track-freq))
(map! (:map sclang-mode-map :localleader :n "t l" 'ziva-goto-track-legato))
(map! (:map sclang-mode-map :localleader :n "t k" 'ziva-goto-track-atk))
(map! (:map sclang-mode-map :localleader :n "t D" 'ziva-goto-track-dec))
(map! (:map sclang-mode-map :localleader :n "t s" 'ziva-goto-track-sus))
(map! (:map sclang-mode-map :localleader :n "t r" 'ziva-goto-track-rel))
;; lfo
(map! (:map sclang-mode-map :localleader :n "l v" 'ziva-lfo-value))
(map! (:map sclang-mode-map :localleader :n "l s" 'ziva-lfo-sine))
(map! (:map sclang-mode-map :localleader :n "l t" 'ziva-lfo-tri))
(map! (:map sclang-mode-map :localleader :n "l z" 'ziva-lfo-saw))
(map! (:map sclang-mode-map :localleader :n "l p" 'ziva-lfo-pulse))
(map! (:map sclang-mode-map :localleader :n "l n r" 'ziva-lfo-noise0))
(map! (:map sclang-mode-map :localleader :n "l n l" 'ziva-lfo-noise1))
(map! (:map sclang-mode-map :localleader :n "l n e" 'ziva-lfo-noise2))
(map! (:map sclang-mode-map :localleader :n "l f" 'ziva-lfo-freq))
(map! (:map sclang-mode-map :localleader :n "l l" 'ziva-lfo-line))
(map! (:map sclang-mode-map :localleader :n "l i" 'ziva-lfo-line-out))
(map! (:map sclang-mode-map :localleader :n "l o" 'ziva-lfo-line-in))

(defun ziva-boot () (interactive) (insert "Ziva.boot;"))

(defun ziva-goto-track (name)
  (interactive "sTrack: ")
  (setq x (search-forward-regexp (format "^~%s" name) nil t))
  (if x
      x
    (search-backward-regexp (format "^~%s" name) nil t)))

(defun ziva-insert-track (track instrument)
  "Insert a new track with a NAME and INSTRUMENT."
  (interactive "sName: \nsInstrument: ")
  (if (search-forward-regexp "^$" nil t)
      nil
    (search-forward-regexp"$")
  (insert (format "~%s s: \\%s >>>.%d 1;" track instrument ziva-mixer-next-available-track)))

(defun ziva-insert-track-num (num)
  (setq track (format "tk%d" num))
  (if (ziva-goto-track track)
      nil
    (ziva-insert-track track "")
    (move-beginning-of-line nil)
    (search-forward "s: \\")
    (move-to-column (- (current-column) 1))
    (evil-append 1)))


(defun ziva-insert-track-1 () (interactive) (ziva-insert-track-num 1))
(defun ziva-insert-track-2 () (interactive) (ziva-insert-track-num 2))
(defun ziva-insert-track-3 () (interactive) (ziva-insert-track-num 3))
(defun ziva-insert-track-4 () (interactive) (ziva-insert-track-num 4))
(defun ziva-insert-track-5 () (interactive) (ziva-insert-track-num 5))
(defun ziva-insert-track-6 () (interactive) (ziva-insert-track-num 6))
(defun ziva-insert-track-7 () (interactive) (ziva-insert-track-num 7))
(defun ziva-insert-track-8 () (interactive) (ziva-insert-track-num 8))
(defun ziva-insert-track-9 () (interactive) (ziva-insert-track-num 9))

(defun ziva-goto-track-param (track param)
  "Go to tha TRACK's PARAMeter value, or create it if is doesn't exist."
  (ziva-goto-track track)
  (if (search-forward-regexp (format "^~%s .* %s: " track param) nil t)
      nil
    (ziva-insert-track-param param)))

(defun ziva-insert-track-param (param)
  (ziva-goto-track-last-param)
  (insert (format " %s: " param))
  (goto-char (- (point) 1))
  (evil-append 1))

(defun ziva-goto-track-last-param ()
  (if (search-forward ">>>")
      (goto-char (- (point) 4))
    (search-forward ";")
    (goto-char (- (point 1)))))

(defun ziva-goto-track-instrument (track) (interactive "sTrack: \n") (ziva-goto-track-param track "s"))
(defun ziva-goto-track-dur (track) (interactive "sTrack: \n") (ziva-goto-track-param track "dur"))
(defun ziva-goto-track-degree (track) (interactive "sTrack: \n") (ziva-goto-track-param track "degree"))
(defun ziva-goto-track-octave (track) (interactive "sTrack: \n") (ziva-goto-track-param track "octave"))
(defun ziva-goto-track-amp (track) (interactive "sTrack: \n") (ziva-goto-track-param track "amp"))
(defun ziva-goto-track-freq (track) (interactive "sTrack: \n") (ziva-goto-track-param track "freq"))
(defun ziva-goto-track-legato (track) (interactive "sTrack: \n") (ziva-goto-track-param track "legato"))
(defun ziva-goto-track-atk (track) (interactive "sTrack: \n") (ziva-goto-track-param track "atk"))
(defun ziva-goto-track-dec (track) (interactive "sTrack: \n") (ziva-goto-track-param track "dec"))
(defun ziva-goto-track-sus (track) (interactive "sTrack: \n") (ziva-goto-track-param track "sus"))
(defun ziva-goto-track-rel (track) (interactive "sTrack: \n") (ziva-goto-track-param track "rel"))


(defun ziva-insert-lfo (name value)
  (search-forward-regexp "$")
  (insert (format "\n~%s lfo: %s;" name value))
  (search-backward-regexp "(")
  (evil-append 1))

(defun ziva-lfo-func (name func)
  (ziva-insert-lfo name (format "%s(, 0, 1)" func)))

(defun ziva-lfo-value (name value) (interactive "sName: \nsValue: ") (ziva-insert-lfo name value))

(defun ziva-lfo-sine (name) (interactive "sName: ") (ziva-lfo-func name "sine"))
(defun ziva-lfo-tri (name) (interactive "sName: ") (ziva-lfo-func name "tri"))
(defun ziva-lfo-saw (name) (interactive "sName: ") (ziva-lfo-func name "saw"))
(defun ziva-lfo-pulse (name) (interactive "sName: ") (ziva-lfo-func name "pulse"))
(defun ziva-lfo-noise0 (name) (interactive "sName: ") (ziva-lfo-func name "noise0"))
(defun ziva-lfo-noise1 (name) (interactive "sName: ") (ziva-lfo-func name "noise1"))
(defun ziva-lfo-noise2 (name) (interactive "sName: ") (ziva-lfo-func name "noise2"))

(defun ziva-lfo-freq (name) (interactive "sName: ") (ziva-lfo-value name "sine(, 50, 15000)"))
(defun ziva-lfo-line (name) (interactive "sName: ") (ziva-lfo-value name "line(, 0, 1)"))
(defun ziva-lfo-line-in (name) (interactive "sName: ") (ziva-lfo-value name "line(, 0, 0.1)"))
(defun ziva-lfo-line-out (name) (interactive "sName: ") (ziva-lfo-value name "line(, 0.1, 0)"))

(defun ziva-increment
    ())

(provide 'ziva)
;;; ziva.el ends here
